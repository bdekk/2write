package nl.bdekk.writeapi.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "project")
@NamedQuery(name=ProjectEntity.FIND_ALL, query="SELECT p FROM ProjectEntity p")
public class ProjectEntity {

    public static final String FIND_ALL = "ProjectEntity.findAll";
    public static final String FIND_BY_NAME = "ProjectEntity.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;
    private String title;

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
}
