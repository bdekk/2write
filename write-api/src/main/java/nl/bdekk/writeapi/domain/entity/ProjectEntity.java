package nl.bdekk.writeapi.domain.entity;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@Table(name = "project")
@NamedQuery(name=ProjectEntity.FIND_ALL, query="SELECT p FROM ProjectEntity p")
public class ProjectEntity {

    public static final String FIND_ALL = "ProjectEntity.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
}
