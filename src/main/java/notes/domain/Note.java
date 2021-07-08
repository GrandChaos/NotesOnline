package notes.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String body;
    private Date lastChange;
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public Note() {};

    public Note(String name, Group group){
        this.name = name;
        this.group = group;
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

    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
    public Date getLastChange() {
        return lastChange;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public Group getGroup() {
        return group;
    }
}
