package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.domain.Project;
import nl.bdekk.writeapi.domain.ProjectFile;
import nl.bdekk.writeapi.domain.entity.FileEntity;
import nl.bdekk.writeapi.domain.entity.ProjectEntity;
import nl.bdekk.writeapi.helpers.FileHelper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectDao {

    @Inject
    private RepositoryConnection con;

    @Inject
    EntityManager manager;

    public ProjectDao() {
    }

    public List<Project> getProjects() {
        List<ProjectEntity> entities = getProjectEntities();
        List<Project> projects = entities.stream()
        .map(entity -> {
            Project project = null;
            try {
                Repository rep = con.getRepo(Paths.get(entity.getDirectory()));
//                List<File> files = con.getFilesFromCommit("HEAD", rep);
                List<FileEntity> files = entity.getFileEntities();
                project = convertRepoToProject(entity, files);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return project;
        }).collect(Collectors.toList());
        return projects;
    }

    public Project getProject(long id) {
        Project project = FileHelper.getProjectEntityById(manager, id).map(entity -> {
            Repository rep = null;
            try {
                rep = con.getRepo(Paths.get(entity.getDirectory()));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            List<File> files = con.getFilesFromCommit("HEAD", rep);
            List<FileEntity> files = entity.getFileEntities();
            return this.convertRepoToProject(entity, files);
        }).orElse(null);
        return project;
    }

    private List<ProjectEntity> getProjectEntities() {
        TypedQuery<ProjectEntity> query = manager.createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class);
        return query.getResultList();
    }

    private Project convertRepoToProject(ProjectEntity entity, List<FileEntity> fileEntities) {
        Project project = new Project();
        project.setTitle(entity.getTitle());
        project.setId(entity.getId());
        project.setDescription(entity.getDescription());

        List<ProjectFile> projectFiles = FileHelper.convertFileEntitiesToProjectFiles(fileEntities);
        project.setFiles(projectFiles);
        return project;
    }

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Repository repository = this.initializeRepo(title);
        Map<String, String> files = con.getFilesFromCommit("HEAD", repository);

        // save project.
        ProjectEntity entity = new ProjectEntity();
        entity.setDescription(description);
        entity.setTitle(title);
        entity.setDirectory(repository.getDirectory().getCanonicalPath());
        this.save(entity);

        // save files
        List<FileEntity> fes = FileHelper.convertFilesToFileEntities(files, entity);
        fes.stream().forEach(this::save);

        // update project.
        entity.setFileEntities(fes);
        this.save(entity);

        Project project = this.convertRepoToProject(entity, fes);
        return project;
    }

    private void save(Object object) {
        manager.getTransaction().begin();
        manager.persist(object);
        manager.getTransaction().commit();
    }



    private Repository initializeRepo(String title) throws IOException, GitAPIException {
        Repository bareRepo = con.createRepo(true, "", title);
        Repository repo = con.cloneRepo(bareRepo, "", title);

        final String chapterOneText = "1. #" + title;
        final String chapterTwoText = "2. #" + title;
        final String chapterThreeText = "3. #" + title;
        con.addFile(repo, "chapter1.md", chapterOneText.getBytes());
        con.addFile(repo, "chapter2.md", chapterTwoText.getBytes());
        con.addFile(repo, "chapter3.md", chapterThreeText.getBytes());
        con.commit(repo, "Added initial chapters.", null);
        con.push(repo);
        return repo;
    }

}
