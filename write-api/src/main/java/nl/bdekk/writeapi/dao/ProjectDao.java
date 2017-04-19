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
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectDao {

    private RepositoryConnection con;

    private List<Project> projects; // later replace this by db.

    public ProjectDao() {
        con = new RepositoryConnection();
        projects = new ArrayList<>();
    }

    public List<Project> getProjects() throws IOException {
        List<Repository> repos = con.getRepos();
        List<Project> projects = repos.stream()
                .map(repository -> {
                    List<String> files = con.getFilesFromCommit(repository);
                    return convertRepoToProject(repository, files);
                })
                .collect(Collectors.toList());

        return projects;
    }

    private Project convertRepoToProject(Repository repository, List<String> files) {
        Project project = new Project();
        project.setName(repository.getDirectory().getParentFile().getName());
        project.setFiles(files);
        return project;
    }

    public Project createProject(String title, String description) throws IOException, GitAPIException {
        Repository repository = this.initializeRepo(title);
        List<String> files = con.getFilesFromCommit(repository);

        Project project = convertRepoToProject(repository, files);

        //add project entry to db.
//        projects.add(project);

        return project;
    }

    private Repository initializeRepo(String title) throws IOException, GitAPIException {
        Repository bareRepo = con.createRepo(true, "", title);
        Repository repo = con.cloneRepo(bareRepo, "", title);

        con.addFile(repo, "chapter1.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter2.md", "Enjoy writing".getBytes());
        con.addFile(repo, "chapter3.md", "Enjoy writing".getBytes());
        con.commit(repo, "Added initial chapters.", null);

        con.push(repo);
        return repo;
    }

}
