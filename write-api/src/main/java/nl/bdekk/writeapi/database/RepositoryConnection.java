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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

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

    /**
     * Gets a  certain file from the repository.
     * Note: Can be refactored. Now traverses the whole tree.
     * @param rev
     * @param repository
     * @param fileName
     * @param directory
     * @return
     */
    public Optional<Map.Entry<String, String>> getFileFromCommit(String rev, Repository repository, String fileName, String directory) {
        Map<String, String> files = getFilesFromCommit(rev, repository);
        Optional<Map.Entry<String, String>> file = files.entrySet()
                .stream()
                .filter(fileFound -> fileFound.getKey().equals(fileName) && fileFound.getValue().equals(directory))
                .findFirst();
        return file;
    }

    /**
     * Gets all files from a repository.
     * @param rev
     * @param repository
     * @return
     */
    public Map<String, String> getFilesFromCommit(String rev, Repository repository) {
       // find the HEAD
        List<File> items = new ArrayList<>();
        Map<String, String> filesFromCommit = new HashMap<>();
        try {
            ObjectId lastCommitId = null;
            // Makes it simpler to release the allocated resources in one go
            ObjectReader reader = repository.newObjectReader();
            lastCommitId = repository.resolve(rev);
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
                        if (treeWalk.isSubtree()) {
                            treeWalk.enterSubtree();
                        } else {
                            String name = treeWalk.getNameString();
                            String path = treeWalk.getPathString().replace(name, "");
                            filesFromCommit.put(name, path);
                        }
                    }
                }

                revWalk.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesFromCommit;
    }

    public String getFileDataFromCommit(String rev, String path, String name, Repository repository) {
        ObjectReader reader = repository.newObjectReader();
        String fileData = "";

        try {
            final ObjectId id = repository.resolve(rev);


            // Get the commit object for that revision
            RevWalk walk = new RevWalk(reader);
            RevCommit commit = walk.parseCommit(id);

            // Get the revision's file tree
            RevTree tree = commit.getTree();
            // .. and narrow it down to the single file's path
            Path filePath = Paths.get(path, name);
            TreeWalk treewalk = TreeWalk.forPath(reader, filePath.toString(), tree);

            if (treewalk != null) {
                // use the blob id to read the file's data
                byte[] data = reader.open(treewalk.getObjectId(0)).getBytes();
                fileData = new String(data, "utf-8");
            }
        } catch(IOException e) {

        } finally {
            reader.close();
        }
        return fileData;
    }

//    try {
//        // Get the commit object for that revision
//        RevWalk walk = new RevWalk(reader);
//        RevCommit commit = walk.parseCommit(id);
//
//        // Get the revision's file tree
//        RevTree tree = commit.getTree();
//        // .. and narrow it down to the single file's path
//        TreeWalk treewalk = TreeWalk.forPath(reader, path, tree);
//
//        if (treewalk != null) {
//            // use the blob id to read the file's data
//            byte[] data = reader.open(treewalk.getObjectId(0)).getBytes();
//            return new String(data, "utf-8");
//        } else {
//            return "";
//        }
//    } finally {
//        reader.release();
//    }

    public File addFile(Repository repository, String fileName, byte[] data) throws IOException, GitAPIException {
        return addFile(repository, fileName, "", data);
    }

    public File addFile(Repository repository, String fileName, String filePath, byte[] data) throws IOException, GitAPIException {
        Path path = Paths.get(repository.getDirectory().getParent(), filePath);
        try (Git git = new Git(repository)) {
            File file = new File(path.toString(), fileName);
            if (!file.exists() && !file.createNewFile()) {
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
