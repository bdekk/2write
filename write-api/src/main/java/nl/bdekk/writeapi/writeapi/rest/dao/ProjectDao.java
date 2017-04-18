package nl.bdekk.writeapi.writeapi.rest.dao;

import nl.bdekk.writeapi.writeapi.rest.database.RepositoryConnection;
import nl.bdekk.writeapi.writeapi.rest.dto.Project;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;

import javax.faces.bean.ApplicationScoped;
import java.io.Console;
import java.io.IOException;
import java.util.Arrays;

@ApplicationScoped
public class ProjectDao {

    private RepositoryConnection con;

    private final String FIRST_FILE_NAME = "chapter1.md";
    private final String FIRST_FILE_DATA = "Enjoy writing!";

    public ProjectDao() {
        con = new RepositoryConnection();
    }

    public void getDocument() {
//        con.getRepo();
    }

    public Project createProject() throws IOException, GitAPIException {
        Repository repo = con.createRepo("book2");
        con.addFile(repo, "chapter1.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter2.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter3.md", "Enjoy writing".getBytes());
        con.commit(repo, "Added initial chapters.");

        Project project = new Project();
        project.setName(repo.getRemoteName(Constants.HEAD));
        project.setFiles(Arrays.asList(repo.getDirectory()));
        return project;
    }


}
