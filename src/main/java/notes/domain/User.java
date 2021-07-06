package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Collection<Group> groups;

    public User() {};

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }
    public Collection<Group> getGroups() {
        return groups;
    }
}
