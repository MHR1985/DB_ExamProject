package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.repository.PersonRepository;
import dk.thesocialnetwork.dto.AjaxDTO;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.UserRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Controller
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private PersonRepository personRepository;
    private final Driver driver;

    public LoginController(Driver driver) {
        this.driver = driver;
        this.personRepository = new PersonRepository(driver);
    }

    @PostMapping("/createuser")
    @Transactional
    public ResponseEntity<AjaxDTO> createUser(@ModelAttribute User user) {
        String username = user.getUsername();
        AjaxDTO ajaxDTO = new AjaxDTO();
        if (userRepository.findUserWithUsername(username) == null && personRepository.getHandleName(username) == null) {
            try(Session session = driver.session()) {
                userRepository.save(new User(username, encoder.encode(user.getPassword())));
                personRepository.createPerson(username);
                ajaxDTO.setSuccess("User created you can now login");
                return new ResponseEntity<>(ajaxDTO, HttpStatus.OK);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
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
    public String getLoginPage() throws IOException {
        return "login";
    }
}
