package nl.bdekk.writeapi.dao;

import nl.bdekk.writeapi.database.RepositoryConnection;
import nl.bdekk.writeapi.dto.Project;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;

import javax.faces.bean.ApplicationScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ProjectDao {

    private RepositoryConnection con;

    private List<Project> projects; // later replace this by db.

    public ProjectDao() {
        con = new RepositoryConnection();
        projects = new ArrayList<>();
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Repository repository = this.initializeRepo(title);

        Project project = new Project();
        project.setName(repository.getRemoteName(Constants.HEAD));
        project.setFiles(Arrays.asList(repository.getDirectory()));

        //add project entry to db.
        projects.add(project);

        return project;
    }

    private Repository initializeRepo(String title) throws IOException, GitAPIException {
        Repository repo = con.createRepo(title);
        con.addFile(repo, "chapter1.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter2.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter3.md", "Enjoy writing".getBytes());
        con.commit(repo, "Added initial chapters.", null);
        return repo;
    }

}
