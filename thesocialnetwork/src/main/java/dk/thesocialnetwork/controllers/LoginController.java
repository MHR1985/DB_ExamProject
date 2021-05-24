package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.AjaxDTO;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/createuser")
    public ResponseEntity<AjaxDTO> createUser(@ModelAttribute User user) {
        AjaxDTO ajaxDTO = new AjaxDTO();
        if (userRepository.findUserWithUsername(user.getUsername()) == null) {
            try {
                User userToCreate = new User(user.getUsername(), encoder.encode(user.getPassword()));
                userRepository.save(userToCreate);
                ajaxDTO.setSuccess("User created you can now login");
                return new ResponseEntity<>(ajaxDTO, HttpStatus.OK);
            } catch (Exception e) {
                ajaxDTO.setError("Error when creating user");
                return new ResponseEntity<>(ajaxDTO, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            String errorMessage = String.format("User with username: %s already exist", user.getUsername());
            ajaxDTO.setFailed(errorMessage);
            return new ResponseEntity<>(ajaxDTO, HttpStatus.CONFLICT);
        }
    }


    @GetMapping("")
    public String getLoginPage() {
        return "login";
    }

}
