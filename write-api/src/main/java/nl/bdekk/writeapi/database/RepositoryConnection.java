package nl.bdekk.writeapi.database;

import nl.bdekk.writeapi.domain.User;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.treewalk.TreeWalk;

import javax.faces.bean.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class RepositoryConnection {

//    FileRepositoryBuilder repositoryBuilder;

    private static Logger LOG = Logger.create(RepositoryConnection.class);

    private final String GIT_DIR = "/git-server/repos/";
    private final String TYPE = ".git";

    private final String HOME_DIR = System.getProperty("user.home");
    private final String GIT_SERVER_PATH = HOME_DIR + GIT_DIR;

    public RepositoryConnection() {

    }

    public Repository getRepo(Path dir) throws IOException {
        return new FileRepositoryBuilder()
                .setMustExist( true )
                .setGitDir(dir.toFile())
                .build();
    }

    public List<Repository> getRepos() throws IOException {
        List<Repository> repos = new ArrayList<>();
        Files.newDirectoryStream(Paths.get(GIT_SERVER_PATH, "bare"), path -> path.toFile().isDirectory())
                .forEach(dir -> {
                    try {
                        Path dirWithSlash = Paths.get(dir.toString(), File.separator);
                        Repository rep =  getRepo(dirWithSlash);
                        repos.add(rep);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return repos;
    }

    public Repository cloneRepo(Repository bareRepo, String folder, String repositoryName) throws GitAPIException {

        Path gitDir = Paths.get(GIT_SERVER_PATH, folder, repositoryName);

//        StoredConfig config = git.getRepository().getConfig();
//        config.setString("remote", "origin", "url", "http://github.com/user/repo");
//        config.save();

        Git git = Git.cloneRepository()
                .setDirectory(gitDir.toFile())
               .setURI(bareRepo.getDirectory().toURI().toString()).call();

        return git.getRepository();
    }

    public Repository createRepo(boolean bare, String folder, String repositoryName) throws IOException, GitAPIException {
        String bareFolder = bare ? "bare" : "";
        String type = bare ? ".git" : "";
        Path gitDir = Paths.get(GIT_SERVER_PATH, bareFolder, folder, repositoryName + type);

        File dir = gitDir.toFile();
        if(dir.exists()) {
            throw new IOException("Project already exists.");
        }

        if(!dir.mkdirs()) {
            throw new IOException("Could not create directory " + dir);
        }

        // create the directory
//        Repository repository = FileRepositoryBuilder.create(new File(dir, TYPE));
//        repository.create(true);

        Git git = Git.init().setDirectory( dir ).setBare( bare ).call();


        return git.getRepository();
    }

    public List<String> getFilesFromCommit(Repository repository) {
       // find the HEAD
        List<String> items = new ArrayList<>();
        try {
            ObjectId lastCommitId = null;
            lastCommitId = repository.resolve(Constants.HEAD);
            if(lastCommitId == null) {
                // could not find a commit?
                LOG.error("Could not find commit when retrieving files for repo " + repository.getDirectory().getAbsolutePath());
            } else {

                RevWalk revWalk = new RevWalk(repository);
                RevCommit commit = revWalk.parseCommit(lastCommitId);
                // and using commit's tree find the path
                RevTree tree = commit.getTree();
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(false);
                    treeWalk.setPostOrderTraversal(false);

                    while (treeWalk.next()) {
                        items.add(treeWalk.getPathString());
                    }
                }

                revWalk.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
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
            CommitCommand command = git.commit().setMessage(message);

            if(author != null) {
                command.setAuthor(author.getUsername(), author.getEmail());
            }

            RevCommit revCommit = command.call();
            return revCommit.getId();
        }
    }

    public boolean push(Repository repo) throws IOException, GitAPIException {
        Git git = Git.open( repo.getDirectory() );
        Set<String> s = repo.getRemoteNames();
        Iterable<PushResult> push = git.push()
                .setForce(true)
                .setPushAll()
                .setPushTags()
                .call();
        return push.iterator().next().getRemoteUpdates().isEmpty();
    }
}
