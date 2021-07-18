package notes.view;

import notes.domain.User;
import notes.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String loginRedirect(Model model) {
        return "redirect:/NotesOnline";
    }

    @GetMapping("/registration")
    public String regForm(Model model) {
        model.addAttribute("regForm",  new RegForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("regForm") RegForm regForm, Model model) {
        User user = userRepository.findByName(regForm.getName());
        if (user == null) {
            if ((regForm.getName().equals("") || regForm.getPassword().equals("") || regForm.getPasswordRepeat().equals(""))) {
                model.addAttribute("regForm", new RegForm());
                model.addAttribute("error", "Fill in all the fields");
                return "registration";
            }
            if (!regForm.getPassword().equals(regForm.getPasswordRepeat())) {
                model.addAttribute("regForm", new RegForm());
                model.addAttribute("error", "Passwords do not match");
                return "registration";
            }
            userRepository.save(new User(regForm.getName(), bCryptPasswordEncoder.encode(regForm.getPassword())));
        } else {
            model.addAttribute("regForm", new RegForm());
            model.addAttribute("error", "A user with this name already exists");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/login";
    }
}

class RegForm {
    private String name;
    private String password;
    private String passwordRepeat;

    public RegForm() { }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }
    public void setPasswordRepeat(String password) {
        this.passwordRepeat = password;
    }
}