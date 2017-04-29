package nl.bdekk.writeapi.domain.entity;


import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name=FileEntity.FIND_ALL, query="SELECT f FROM FileEntity f"),
        @NamedQuery(name = FileEntity.FIND_BY_ID, query = "SELECT f FROM FileEntity f WHERE f.id = :id")
})
@Table(name = "file")
public class FileEntity {

    public static final String FIND_ALL = "FileEntity.findAll";
    public static final String FIND_BY_ID = "FileEntity.findById";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    private ProjectEntity project;

    private String name;

    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
