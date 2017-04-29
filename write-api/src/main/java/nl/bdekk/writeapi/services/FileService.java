package nl.bdekk.writeapi.services;

import nl.bdekk.writeapi.dao.FileDao;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FileService {

    @Inject
    private FileDao dao;

    public String getFile(long fileId) {
        return dao.getFile(fileId);
    }
}
