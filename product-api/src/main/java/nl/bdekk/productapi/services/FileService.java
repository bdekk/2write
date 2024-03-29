package nl.bdekk.productapi.services;

import nl.bdekk.productapi.dao.FileDao;
import nl.bdekk.productapi.domain.ProjectFile;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class FileService {

    @Inject
    private FileDao dao;

    public String getFile(long fileId) {
        return dao.getFile(fileId);
    }

    public boolean updateFile(long fileId, ProjectFile file) {
        return dao.updateFile(fileId, file);
    }

    public ProjectFile createFile(long projectId, String name) throws IOException, GitAPIException {
        return dao.createFile(projectId, name);
    }
}
