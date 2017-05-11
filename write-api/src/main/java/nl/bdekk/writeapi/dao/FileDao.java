package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.domain.ProjectFile;
import nl.bdekk.writeapi.domain.entity.FileEntity;
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
import java.util.Optional;

@ApplicationScoped
public class FileDao {


    @Inject
    EntityManager manager;

    @Inject
    private RepositoryConnection con;

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
        if(fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();
            String projectDirectory = file.getProject().getDirectory();
            String path = file.getPath();
            try {
                Repository repository = con.getRepo(Paths.get(projectDirectory));
                con.addFile(repository, file.getName(), path, updatedFile.getContent().getBytes());
                ObjectId commit = con.commit(repository, "updated file" + file.getName(), null);
                con.push(repository);
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }

            manager.getTransaction().begin();
            if (!updatedFile.getName().equals(updatedFile.getName())) {
                file.setName(updatedFile.getName());
            }
            manager.getTransaction().commit();
        }
        return true;
    }
}
