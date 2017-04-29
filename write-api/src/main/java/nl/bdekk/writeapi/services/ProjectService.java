package nl.bdekk.writeapi.services;

import nl.bdekk.writeapi.dao.ProjectDao;
import nl.bdekk.writeapi.domain.Project;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class ProjectService {

    @Inject
    private ProjectDao dao;

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Project project = null;
        project = dao.createProject(title, description);
        return project;
    }

    public List<Project> getProjects() throws IOException {
        return dao.getProjects();
    }

    public Project getProject(long id) {
        return dao.getProject(id);
    }
}
