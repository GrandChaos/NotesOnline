package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "user_t")
public class User implements Comparable<User> {

    @Id
    private String name;
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Group> groups = new TreeSet<>();
    @JoinTable(
            name = "share_t",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "note")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "sharedNotes")
    private Set<Note> sharedNotes = new TreeSet<>();

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
        return new TreeSet<>(this.groups);
    }

    public Set<Note> getSharedNotes() {
        return new TreeSet<>(this.sharedNotes);
    }
    public void setSharedNotes(TreeSet<Note> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    public void addGroup(Group group){
        groups.add(group);
    }
    public void deleteGroup(Group group){
        groups.remove(group);
    }

    public void addSharedNote(Note shredNote){
        sharedNotes.add(shredNote);
    }
    public void deleteSharedNote(Note sharedNote){
        sharedNotes.remove(sharedNote);
    }

    @Override
    public int compareTo(User user){
        return name.compareTo(user.getName());
    }
}
