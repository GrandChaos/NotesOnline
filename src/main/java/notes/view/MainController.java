package notes.view;

import notes.domain.Group;
import notes.domain.Note;
import notes.domain.User;
import notes.services.GroupRepository;
import notes.services.NoteRepository;
import notes.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Date;
import java.util.TreeSet;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    NoteRepository noteRepository;

    @GetMapping("/NotesOnline")
    public String start (Model model, Principal principal){
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        Displayed displayed = new Displayed();
        displayed.setNothing();
        model.addAttribute("displayed", displayed);

        //System.out.println(user.getGroups());
        return "main";
    }

    @GetMapping("/NotesOnline/newNote")
    public String getNewNote (Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        Displayed displayed = new Displayed();
        displayed.setAddNote();
        model.addAttribute("displayed", displayed);

        model.addAttribute("newNote", new NewNote());

        return "main";
    }

    @PostMapping("/NotesOnline/newNote")
    public String postNewNote (Model model, Principal principal, NewNote newNote){
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("newNote", newNote);

        Group group = groupRepository.findByUserAndName(user, newNote.getGroup());

        if (newNote.getGroup().equals("") || newNote.getName().equals(""))
            return "redirect:/NotesOnline";

        if (group == null) {
            group = new Group(newNote.getGroup(), user);
            groupRepository.save(group);
        }
        noteRepository.save(new Note(newNote.getName(), new Date(), group));
        groupRepository.save(group);
        userRepository.save(user);
        return "redirect:/NotesOnline";
    }

    @GetMapping("/NotesOnline/note")
    public String showNote (@RequestParam(value = "id") String id, Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        Displayed displayed = new Displayed();
        displayed.setNote();

        try {
            Note note = noteRepository.getOne(Long.parseLong(id));
            if (note.getGroup().getUser().equals(user) || note.getSharedUsers().contains(user)){ //???????? ???????????????????????? ?????????? ????????????
                if (note.getGroup().getUser().equals(user)) {
                    displayed.setOwner();
                }
                model.addAttribute("note", note);
                model.addAttribute("displayed", displayed);
                return "main";
            }
        }
        catch (EntityNotFoundException e){
            System.out.println(e.fillInStackTrace());
        }

        return "redirect:/NotesOnline";
    }

    @PostMapping("/NotesOnline/note")
    public String postNote (@RequestParam(value = "id") String id, Model model, Principal principal, Note changedNote) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("note", changedNote);

        Note note = noteRepository.getOne(Long.parseLong(id));
        note.setName(changedNote.getName());
        note.setBody(changedNote.getBody());
        note.setDate(new Date());
        //System.out.println(note.getGroup().getName() + " " + note.getName() + " " + note.getDate() + " " + note.getBody());
        noteRepository.save(note);

        return "redirect:/NotesOnline/note?id=" + id;
    }

    @RequestMapping("/NotesOnline/delete")
    public String deleteNote (@RequestParam(value = "id") String id, Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        try {
            Note note = noteRepository.getOne(Long.parseLong(id));

            Group group = note.getGroup();

            if (group.getUser().equals(user) || note.getSharedUsers().contains(user)){ //???????? ???????????????????????? ?????????? ????????????
                group.deleteNote(note);
                //System.out.println(group.getNotes());
                groupRepository.save(group);
            }

            if (group.getNotes().isEmpty()){
                user.deleteGroup(group);
                userRepository.save(user);
            }
        }
        catch (EntityNotFoundException e){
            System.out.println(e.fillInStackTrace());
        }

        return "redirect:/NotesOnline";
    }

    @GetMapping("/NotesOnline/byGroup")
    public String searchByGroup (@RequestParam(value = "search") String search, Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        TreeSet<Note> result = new TreeSet<>();
        for (Group group : user.getGroups()){
            if (group.getName().contains(search)){
                 for (Note note : group.getNotes()){
                     if (note.getBody() != null && note.getBody().length() > 23){
                         note.setBody(note.getBody().substring(0, 20) + "...");
                     }
                     result.add(note);
                 }
            }
        }

        for (Note note : user.getSharedNotes()){
            if (note.getGroup().getName().contains(search)){
                if (note.getBody() != null && note.getBody().length() > 23){
                    note.setBody(note.getBody().substring(0, 20) + "...");
                }
                result.add(note);
            }
        }

        model.addAttribute("result", result);

        return "search";
    }

    @GetMapping("/NotesOnline/byName")
    public String searchByName (@RequestParam(value = "search") String search, Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        TreeSet<Note> result = new TreeSet<>();
        for (Group group : user.getGroups()){
            for (Note note : group.getNotes()){
                if (note.getName() != null && note.getName().contains(search)){
                    if (note.getBody() != null && note.getBody().length() > 23){
                        note.setBody(note.getBody().substring(0, 20) + "...");
                    }
                    result.add(note);
                }
            }
        }

        for (Note note : user.getSharedNotes()){
            if (note.getName() != null && note.getName().contains(search)){
                if (note.getBody() != null && note.getBody().length() > 23){
                    note.setBody(note.getBody().substring(0, 20) + "...");
                }
                result.add(note);
            }
        }

        model.addAttribute("result", result);

        return "search";
    }

    @GetMapping("/NotesOnline/byBody")
    public String searchByBody (@RequestParam(value = "search") String search, Model model, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        TreeSet<Note> result = new TreeSet<>();
        for (Group group : user.getGroups()){
            for (Note note : group.getNotes()){
                if (note.getBody() != null && note.getBody().contains(search)){
                    if (note.getBody() != null && note.getBody().length() > 23){
                        note.setBody(note.getBody().substring(0, 20) + "...");
                    }
                    result.add(note);
                }
            }
        }

        for (Note note : user.getSharedNotes()){
            if (note.getBody() != null && note.getBody().contains(search)){
                if (note.getBody() != null && note.getBody().length() > 23){
                    note.setBody(note.getBody().substring(0, 20) + "...");
                }
                result.add(note);
            }
        }

        model.addAttribute("result", result);

        return "search";
    }

    @GetMapping("/NotesOnline/addUserToNote")
    public String addUserGet(@RequestParam(value = "id") String id, Model model, Principal principal){
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);

        Displayed displayed = new Displayed();
        displayed.setAddUser();
        model.addAttribute("displayed", displayed);

        model.addAttribute("userString", new String());

        try {
            Note note = noteRepository.getOne(Long.parseLong(id));
            if (note.getGroup().getUser().equals(user)) {
                model.addAttribute("note", note);
                return "main";
            }
        }
        catch (EntityNotFoundException e){
            System.out.println(e.fillInStackTrace());
        }

        return "redirect:/NotesOnline";
    }

    @PostMapping("/NotesOnline/addUserToNote")
    public String addUserPost(@RequestParam(value = "id") String id, Model model, Principal principal, @RequestParam String userString){
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userString", userString);

        System.out.println("???????????? ?????? ????????????????????????: " + userString);

        try {
            User userToAdd = userRepository.findByName(userString);
            Note note = noteRepository.getOne(Long.parseLong(id));

            userToAdd.addSharedNote(note);
            note.addSharedUser(userToAdd);

            userRepository.save(userToAdd);
            noteRepository.save(note);

            System.out.println(userToAdd.getSharedNotes());
        }
        catch (NullPointerException | EntityNotFoundException e ){
            System.out.println(e.fillInStackTrace());
        }

        return "redirect:/NotesOnline/note?id=" + id;
    }

    @RequestMapping("/NotesOnline/deleteUserFromNote")
    public String removeUser(@RequestParam(value = "id") String id,@RequestParam(value = "username") String username, Model model, Principal principal){
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("username", username);

        try {
            Note note = noteRepository.getOne(Long.parseLong(id));
            User userToRemove = userRepository.findByName(username);

            if (note.getGroup().getUser().equals(user)){
                note.deleteSharedUser(userToRemove);
                userToRemove.deleteSharedNote(note);

                noteRepository.save(note);
                userRepository.save(user);
            }
        }
        catch (EntityNotFoundException | NullPointerException e){
            System.out.println(e.fillInStackTrace());
        }

        return "redirect:/NotesOnline/note?id=" + id;
    }
}

//?????????????????????????????? ?????????? ?????? ?????????????????????? ???????????? ???????????? ????????????????????????
class Displayed {
    private boolean nothing;
    private boolean note;
    private boolean addNote;
    private boolean owner;
    private boolean addUser;

    Displayed() {
        this.nothing = false;
        this.note = false;
        this.addNote = false;
        this.owner = false;
        addUser = false;
    }

    public boolean isNothing() {
        return nothing;
    }
    public void setNothing() {
        this.nothing = true;
        this.note = false;
        this.addNote = false;
        this.owner = false;
    }

    public boolean isNote() {
        return note;
    }
    public void setNote() {
        this.nothing = false;
        this.note = true;
        this.addNote = false;
        this.owner = false;
    }


    public boolean isAddNote() {
        return addNote;
    }
    public void setAddNote() {
        this.nothing = false;
        this.note = false;
        this.addNote = true;
        this.owner = false;
    }

    public boolean isOwner() {
        return owner;
    }
    public void setOwner() {
        this.owner = true;
    }

    public boolean isAddUser() {
        return addUser;
    }

    public void setAddUser() {
        this.addUser = true;
        this.nothing = false;
        this.note = false;
        this.addNote = false;
        this.owner = false;
    }
}

//?????????????????????????????? ?????????? ?????? ???????????? ?????????? ???????????????? ?????????? ??????????????
class NewNote{
    private String group;
    private String name;

    NewNote() {}

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}