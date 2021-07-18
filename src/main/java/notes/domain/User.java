package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "user_t")
public class User {

    @Id
    private String name;
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Group> groups = new TreeSet<>();


    public User() { }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setGroups(TreeSet<Group> groups) {
        this.groups = groups;
    }
    public TreeSet<Group> getGroups() {
        TreeSet<Group> groups = new TreeSet<>();
        for (Group group : this.groups){
            groups.add(group);
        }
        return groups;
    }

    public void addGroup(Group group){
        groups.add(group);
    }
}
