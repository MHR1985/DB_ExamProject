package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.repository.PersonRepository;
import org.neo4j.driver.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/person")
@CrossOrigin
public class PersonController {
    private PersonRepository personRepository;
    private final Driver driver;

    public PersonController(Driver driver) {
        this.driver = driver;
        personRepository = new PersonRepository(driver);
    }

    @Transactional
    @PostMapping(path = "/follow", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> createRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try {
            personRepository.deleteFollower(followsDTO);
            personRepository.createFollower(followsDTO);
            return new ResponseEntity(followsDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(followsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/unfollow", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> deleteRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try {
            personRepository.deleteFollower(followsDTO);
            return new ResponseEntity(followsDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(followsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public String getPeople(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<String> persons = personRepository.getAllNotFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "people";
    }

    @GetMapping("/following")
    public String getFollowing(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<String> persons = personRepository.getAllFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "following";
    }
}
