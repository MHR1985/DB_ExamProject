package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.model.Person;
import dk.thesocialnetwork.repository.PersonRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // Template for creating some start users
    @PostMapping("/create")
    public void createPeople() {
        try (Session session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
            session.run("DROP CONSTRAINT uniqueName if exists");
            session.run("CREATE CONSTRAINT uniqueName on (n:Person) assert n.handleName is unique");
            session.run("WITH ['Amy','Bob','Cal','Dan','Eve', 'Svend', 'Erik', 'Susanne', 'Pernille', 'Per'] AS handleNames " +
                    "UNWIND handleNames AS handleName " +
                    "CREATE (:Person {handleName: handleName})");
            createRelationShipPerson(new FollowsDTO("Amy", "Bob"));
            createRelationShipPerson(new FollowsDTO("Amy", "Svend"));
            createRelationShipPerson(new FollowsDTO("Erik", "Bob"));
            createRelationShipPerson(new FollowsDTO("Susanne", "Dan"));
            createRelationShipPerson(new FollowsDTO("Per", "Bob"));
            createRelationShipPerson(new FollowsDTO("Dan", "Eve"));

        }
    }

    // Template for cypher scripting with COUNT
    @GetMapping("/averageFollowers")
    public ResponseEntity followerAverage( ) {
        List<Record> recordStream;
       try(Session session = driver.session()) {
           Result result = session.run("MATCH (p:Person)-[r: FOLLOWED_BY]->()\n" +
                    "WITH COUNT(r) AS amount_followers\n" +
                    "MATCH (n:Person)\n" +
                    "WITH COUNT(n) as amount_persons, amount_followers\n" +
                    "RETURN sum(toFloat(amount_followers))/amount_persons as averageFollowers");
           recordStream = result.stream().collect(Collectors.toList());

       }
        return new ResponseEntity<>(recordStream.toString(), HttpStatus.OK);

    }

    @GetMapping("")
    public String getPeople(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //need sorting in query, using findall for now
        List<String> persons = personRepository.getAllNotFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "people";
    }

    @GetMapping("/following")
    public String getFollowing(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //find the people the user is following
        List<String> persons = personRepository.getAllFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "following";
    }



}
