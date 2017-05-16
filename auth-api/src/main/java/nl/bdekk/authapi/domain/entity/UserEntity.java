package nl.bdekk.authapi.domain.entity;


import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name= UserEntity.FIND_ALL, query="SELECT f FROM UserEntity f"),
        @NamedQuery(name = UserEntity.FIND_BY_ID, query = "SELECT f FROM UserEntity f WHERE f.id = :id"),
        @NamedQuery(name = UserEntity.FIND_BY_USERNAME_PASSWORD, query="SELECT u FROM UserEntity f WHERE f.username = :username AND f.password = :password")
})
@Table(name="\"user\"")
public class UserEntity {

    public static final String FIND_ALL = "UserEntity.findAll";
    public static final String FIND_BY_ID = "UserEntity.findById";
    public static final String FIND_BY_USERNAME_PASSWORD = "UserEntity.findByUsernamePassword";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstname;

    private String lastname;

    private String username;

    private byte[] password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getUserName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
