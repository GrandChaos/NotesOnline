package notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private Collection<Note> notes;

    public Group() {};

    public Group(String name, User user){
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setNotes(Collection<Note> notes) {
        this.notes = notes;
    }
    public Collection<Note> getNotes() {
        return notes;
    }
}
