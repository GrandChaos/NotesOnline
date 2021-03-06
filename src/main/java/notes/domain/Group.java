package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "group_t")
public class Group implements Comparable<Group>{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Note> notes = new TreeSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    public Group() { }

    public Group(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setNotes(TreeSet<Note> notes) {
        this.notes = notes;
    }
    public TreeSet<Note> getNotes() {
        return new TreeSet<>(this.notes);
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void deleteNote(Note note){
        notes.remove(note);
    }

    @Override
    public int compareTo(Group group) {
        int comp = name.compareTo(group.getName());
        if (comp == 0) {
            return id.compareTo(group.getId());
        }
        return comp;
    }
}
