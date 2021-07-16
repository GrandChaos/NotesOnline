package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_t")
public class Group {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Note> notes = new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    public Group() { }

    public Group(String name) {
        this.name = name;
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

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
    public Set<Note> getNotes() {
        return notes;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
