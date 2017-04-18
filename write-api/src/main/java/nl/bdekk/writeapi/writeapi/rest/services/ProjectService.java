package nl.bdekk.writeapi.writeapi.rest.services;

import nl.bdekk.writeapi.writeapi.rest.dao.ProjectDao;
import nl.bdekk.writeapi.writeapi.rest.dto.Project;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class ProjectService {

    @Inject
    private ProjectDao dao;

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Project project = null;
        project = dao.createProject(title, description);
        return project;
    }
}
