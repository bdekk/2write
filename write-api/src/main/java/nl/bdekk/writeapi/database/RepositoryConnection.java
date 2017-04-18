package nl.bdekk.writeapi.database;

import nl.bdekk.writeapi.dto.User;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import javax.faces.bean.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class RepositoryConnection {

//    FileRepositoryBuilder repositoryBuilder;

    private final String GIT_DIR = "/git-server/repos/";
    private final String TYPE = ".git";

    public RepositoryConnection() {

    }

    public Repository getRepo(Path dir) throws IOException {
        return new FileRepositoryBuilder()
                .setMustExist( true )
                .setGitDir(new File(String.valueOf(dir)))
                .build();
    }

    public Repository createRepo(String repositoryName) throws IOException {
        // prepare a new folder
        final String HOME_DIR = System.getProperty("user.home");
        File dir = new File(HOME_DIR + GIT_DIR + "/" + repositoryName + TYPE);
        if(!dir.exists()) {
            throw new IOException("Project already exists.");
        }

        if(!dir.mkdirs()) {
            throw new IOException("Could not create directory " + dir);
        }

        // create the directory
        Repository repository = FileRepositoryBuilder.create(new File(dir, ".git"));
        repository.create();

        return repository;
    }

    public File addFile(Repository repository, String fileName, byte[] data) throws IOException, GitAPIException {

        try (Git git = new Git(repository)) {
            File file = new File(repository.getDirectory().getParent(), fileName);
            if (!file.createNewFile()) {
                throw new IOException("Could not create file " + file);
            }
            Files.write(file.toPath(), data);

            // run the add-call
            git.add()
                    .addFilepattern(".")
                    .call();
            return file;
        }
    }

    public ObjectId commit(Repository repository, String message, User author) throws GitAPIException {
        try (Git git = new Git(repository)) {
            RevCommit com = git.commit()
                    .setMessage(message)
                    .setAuthor(author.getUsername(), author.getEmail())
                    .call();
            return com.getId();
        }
    }
}
