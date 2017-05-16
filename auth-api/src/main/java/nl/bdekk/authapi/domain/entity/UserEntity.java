package nl.bdekk.authapi.domain.entity;


import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name= UserEntity.FIND_ALL, query="SELECT f FROM UserEntity f"),
        @NamedQuery(name = UserEntity.FIND_BY_ID, query = "SELECT f FROM UserEntity f WHERE f.id = :id")
})
@Table(name="\"user\"")
public class UserEntity {

    public static final String FIND_ALL = "UserEntity.findAll";
    public static final String FIND_BY_ID = "UserEntity.findById";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstname;

    private String lastname;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
