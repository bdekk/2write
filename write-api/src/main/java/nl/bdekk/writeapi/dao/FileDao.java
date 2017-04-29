package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.domain.entity.FileEntity;
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
                fileData = con.getFileDataFromCommit("HEAD", file.getPath(), repository);
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

}
