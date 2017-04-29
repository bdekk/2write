package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.domain.Project;
import nl.bdekk.writeapi.domain.entity.ProjectEntity;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
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
                        List<String> files = con.getFilesFromCommit(rep);
                        project = convertRepoToProject(entity, files);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return project;
                }).collect(Collectors.toList());
        return projects;
    }

    public Project getProject(long id) {
        Project project = this.getProjectEntityById(id).map(entity -> {
            Repository rep = null;
            try {
                rep = con.getRepo(Paths.get(entity.getDirectory()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> files = con.getFilesFromCommit(rep);
            return this.convertRepoToProject(entity, files);
        }).orElse(null);
        return project;
    }

    private List<ProjectEntity> getProjectEntities() {
        TypedQuery<ProjectEntity> query = manager.createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class);
        return query.getResultList();
    }

    private Optional<ProjectEntity> getProjectEntityById(long id) {
        TypedQuery<ProjectEntity> query = manager.createNamedQuery(ProjectEntity.FIND_BY_ID, ProjectEntity.class);
        query = query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

//    private ProjectEntity getProjectEntity(String name) {
//        CriteriaBuilder builder = helper.getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<ProjectEntity> criteria = builder.createQuery(ProjectEntity.class);
//        Root<ProjectEntity> from = criteria.from(ProjectEntity.class);
//        criteria.select(from);
//        criteria.where(builder.equals(from.get(ProjectEntity_.name), name));
//        TypedQuery<ProjectEntity> typed = helper.getEntityManager().createQuery(criteria);
//        try {
//            return typed.getSingleResult();
//        } catch (final NoResultException nre) {
//            return null;
//        }
//    }

    private Project convertRepoToProject(ProjectEntity entity, List<String> files) {
        Project project = new Project();
        project.setTitle(entity.getTitle());
        project.setId(entity.getId());
        project.setDescription(entity.getDescription());
        project.setFiles(files);
        return project;
    }

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Repository repository = this.initializeRepo(title);
        List<String> files = con.getFilesFromCommit(repository);

        ProjectEntity entity = new ProjectEntity();
        entity.setDescription(description);
        entity.setTitle(title);
        entity.setDirectory(repository.getDirectory().getCanonicalPath());
        this.save(entity);

        Project project = this.convertRepoToProject(entity, files);

        //add project entry to db.
//        projects.add(project);

        return project;
    }

    private void save(ProjectEntity object) {
        manager.getTransaction().begin();
        manager.persist(object);
        manager.getTransaction().commit();

    }

    private Repository initializeRepo(String title) throws IOException, GitAPIException {
        Repository bareRepo = con.createRepo(true, "", title);
        Repository repo = con.cloneRepo(bareRepo, "", title);

        final String text = "# "+ title;
        final String chapterOneText = "1. " + text;
        final String chapterTwoText = "2. " + text;
        final String chapterThreeText = "3. " + text;
        con.addFile(repo, "chapter1.md", chapterOneText.getBytes());
        con.addFile(repo, "chapter2.md", chapterTwoText.getBytes());
        con.addFile(repo, "chapter3.md", chapterThreeText.getBytes());
        con.commit(repo, "Added initial chapters.", null);

        con.push(repo);
        return repo;
    }

}
