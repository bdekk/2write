package nl.bdekk.writeapi.domain.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name=ProjectEntity.FIND_ALL, query="SELECT p FROM ProjectEntity p"),
        @NamedQuery(name = ProjectEntity.FIND_BY_ID, query = "SELECT c FROM ProjectEntity c WHERE c.id = :id")
})
@Table(name = "project")
public class ProjectEntity {

    public static final String FIND_ALL = "ProjectEntity.findAll";
    public static final String FIND_BY_ID = "ProjectEntity.findById";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "project")
    private List<FileEntity> fileEntities = new ArrayList<>();

    private String name;

    private String description;
    private String title;

    private String directory;

//    @OneToMany
//    @JoinColumn(name = "commiter")
//    private List<User> commiters;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<FileEntity> getFileEntities() {
        return fileEntities;
    }

    public void setFileEntities(List<FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }
}
