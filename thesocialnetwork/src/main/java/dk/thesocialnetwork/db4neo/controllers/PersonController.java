package dk.thesocialnetwork.db4neo.controllers;

import dk.thesocialnetwork.db4neo.dto.FollowsDTO;
import dk.thesocialnetwork.db4neo.model.Person;
import dk.thesocialnetwork.db4neo.repository.PersonRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    private PersonRepository personRepository;

    private final Driver driver;


    public PersonController(PersonRepository personRepository, Driver driver) {
        this.personRepository = personRepository;
        this.driver = driver;
    }

    @GetMapping
    public Iterable<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/{name}")
    public Person getPersonByName(@PathVariable String name) {
        return personRepository.getPersonByHandleName(name);
    }

    @PostMapping(path = "/follows", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> createRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '" + followsDTO.follower + "'}) " +
                    "MATCH (m:Person {handleName: '" + followsDTO.target + "'}) " +
                    "DROP (n)-[: FOLLOWS]->(m) if exists " +
                    "DROP (n)<-[: FOLLOWED_BY]-(m) if exists " +
                    "CREATE (n)-[: FOLLOWS]->(m)" +
                    "CREATE (n)<-[: FOLLOWED_BY]-(m)");
            return new ResponseEntity(followsDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(followsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




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

    @PostMapping("/similarlikes")
    public ResponseEntity<Object> similarLikes( ) {
        try(Session session = driver.session()) {
            Result graphExists = session.run(("CALL gds.graph.exists('similarLikes') YIELD exists"));
            Record record = graphExists.single();
            if(!record.get("exists").toString().equals("FALSE"))
                session.run("CALL gds.graph.drop('similarLikes')");
            String similarGraph = "CALL gds.graph.create(" +
                    "    'similarLikes'," +
                    "    ['Post', 'Person']," +
                    "    {" +
                    "        Liked: {" +
                    "            type: 'LIKED'" +
                    "        }" +
                    "    }" +
                    ")";
            session.run(similarGraph);
            Result result = session.run("CALL gds.nodeSimilarity.stream('similarLikes') " +
                    "YIELD node1, node2, similarity " +
                    "RETURN gds.util.asNode(node1).handleName AS Person1, gds.util.asNode(node2).handleName AS Person2, similarity " +
                    "ORDER BY similarity DESCENDING, Person1, Person2");
            List<Record> recordStream = result.stream().collect(Collectors.toList());
            return new ResponseEntity<>(recordStream.toString(), HttpStatus.OK);
        }
    }


}
