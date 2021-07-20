package notes.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "note_t")
public class Note implements Comparable<Note> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String body;
    private Date date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_t", nullable = false)
    private Group group;

    public Note() { }

    public Note(String name, Date date, Group group) {
        this.name = name;
        this.date = date;
        this.group = group;
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

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public Group getGroup() {
        return group;
    }

    @Override
    public int compareTo(Note note) {
        int comp = name.compareTo(note.getName());
        if (comp == 0){
            comp = id.compareTo(note.getId());
        }
        return comp;
    }
}
