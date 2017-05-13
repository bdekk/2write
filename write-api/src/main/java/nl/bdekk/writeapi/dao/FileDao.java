package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.domain.ProjectFile;
import nl.bdekk.writeapi.domain.entity.FileEntity;
import nl.bdekk.writeapi.domain.entity.ProjectEntity;
import nl.bdekk.writeapi.helpers.FileHelper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileDao {


    @Inject
    EntityManager manager;

    @Inject
    private RepositoryConnection con;

    private final String FILE_TYPE = "md";

    public String getFile(long fileId) {
        Optional<FileEntity> fileOpt = getFileEntity(fileId);
        String fileData = null;
        if(fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();
            String projectDirectory = file.getProject().getDirectory();
            try {
                Repository repository = con.getRepo(Paths.get(projectDirectory));
                fileData = con.getFileDataFromCommit("HEAD", file.getPath(), file.getName(), repository);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileData;
    }

    private Optional<FileEntity> getFileEntity(long id) {
        TypedQuery<FileEntity> query = manager.createNamedQuery(FileEntity.FIND_BY_ID, FileEntity.class);
        query = query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    public boolean updateFile(long fileId, ProjectFile updatedFile) {
        Optional<FileEntity> fileOpt = getFileEntity(fileId);
        ObjectId commit = null;
        if(fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();
            String projectDirectory = file.getProject().getDirectory();
            String path = file.getPath();
            try {
                Repository repository = con.getRepo(Paths.get(projectDirectory));
                con.addFile(repository, file.getName(), path, updatedFile.getContent().getBytes());
                commit = con.commit(repository, "updated file" + file.getName(), null);
                con.push(repository);
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }

            if (updatedFile.getName() != null && !file.getName().equals(updatedFile.getName())) {
                file.setName(updatedFile.getName());
            }
            this.save(file);
        }
        return commit != null;
    }

    public ProjectFile createFile(long projectId, String name) throws IOException, GitAPIException {
        Optional<ProjectEntity> entity = FileHelper.getProjectEntityById(manager, projectId);
        if(!entity.isPresent()) {
            return null;
        }

        ProjectEntity projectEntity = entity.get();
        Repository repo = con.getRepo(Paths.get(projectEntity.getDirectory()));

        final String fileText = "#" + name + "\n Happy writing!";
        con.addFile(repo, name + "." + FILE_TYPE, fileText.getBytes());
        con.commit(repo, "Added file." + name, null);
        con.push(repo);

        Optional<Map.Entry<String, String>> file = con.getFileFromCommit("HEAD", repo, name, "");
        ProjectFile pf = null;
        if(file.isPresent()) {
            FileEntity fe = FileHelper.convertFileToFileEntity(file.get(), projectEntity);
            this.save(fe);
            pf = FileHelper.convertFileEntityToProjectFile(fe);
        }
        return pf;
    }

    private void save(Object object) {
        manager.getTransaction().begin();
        manager.persist(object);
        manager.getTransaction().commit();
    }
}
