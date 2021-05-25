package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.model.Person;
import dk.thesocialnetwork.repository.PersonRepository;
import org.neo4j.driver.Driver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/people")
@CrossOrigin
public class PeopleController {

    private PersonRepository personRepository;

    private final Driver driver;


    public PeopleController(PersonRepository personRepository, Driver driver) {
        this.personRepository = personRepository;
        this.driver = driver;
    }

    @GetMapping("")
    public String getPeople(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //need sorting in query, using findall for now
        List<Person> persons =  personRepository.findAll();
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "people";
    }

    @GetMapping("/following")
    public String getFollowing(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //find the people the user is following
        List<Person> persons =  personRepository.findAll();
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "following";
    }

}
