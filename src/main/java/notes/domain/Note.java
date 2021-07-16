package notes.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "note_t")
public class Note {

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

    public Note(String name, Date date, String body) {
        this.name = name;
        this.date = date;
        this.body = body;
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
}
