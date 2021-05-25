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


    public PersonController(PersonRepository personRepository, Driver driver) {
        this.personRepository = personRepository;
        this.driver = driver;
    }

    /*@GetMapping("")
    public Iterable<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/{name}")
    public Person getPersonByName(@PathVariable String name) {
        return personRepository.getPersonByHandleName(name);
    }

    public Iterable<Person> findAllToFollow() {

        return personRepository.findAll();
    }*/

    @Transactional
    @PostMapping(path = "/follow", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> createRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try {
            deleteFollower(followsDTO);
            createFollower(followsDTO);
            return new ResponseEntity(followsDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(followsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteFollower(FollowsDTO dto){
        try (Session session = driver.session()){
            session.run("MATCH (n:Person {handleName: '"+ dto.follower + "'}) " +
                    "MATCH (m:Person {handleName: '"+dto.target+"'}) " +
                    "MATCH (n)-[r:FOLLOWS]->(m) " +
                    "DELETE r " +
                    "WITH n, m " +
                    "MATCH (n)<-[r:FOLLOWED_BY]-(m) " +
                    "DELETE r");
        }
    }
    private void createFollower(FollowsDTO dto){
        try (Session session = driver.session()){
            session.run("MATCH (n:Person {handleName: '"+ dto.follower + "'}) " +
                    "MATCH (m:Person {handleName: '"+dto.target+"'}) " +
                    "CREATE (n)-[:FOLLOWS]->(m) " +
                    "CREATE (n)<-[:FOLLOWED_BY]-(m)");
        }
    }

    @PostMapping(path = "/unfollow", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> deleteRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try {
            deleteFollower(followsDTO);
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

    public void createPerson(String username) {
        try (Session session = driver.session()) {
            session.run("CREATE (:Person {handleName: "+username+"})");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @GetMapping("")
    public String getPeople(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //need sorting in query, using findall for now
        List<String> persons = getAllNotFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "people";
    }

    @GetMapping("/following")
    public String getFollowing(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //find the people the user is following
        List<String> persons = getAllFollowing(userName);
        model.addAttribute("people",persons);
        model.addAttribute("username",userName);
        return "following";
    }

    private List<String> getAllFollowing(String handlename){
        List<Record> recordStream;
        List<String> following = new ArrayList<>();
        try(Session session = driver.session()){
            Result result = session.run("MATCH (n:Person {handleName: '"+handlename+"'}) " +
                    "MATCH (n)-[r:FOLLOWS]->(m) " +
                    "return m");
            recordStream = result.stream().collect(Collectors.toList());
            for (Record rec: recordStream) {
                // rec.get(m) BECAUSE WE WE RETURN M IN CYPHER THIS IS THE RECORD'S NAME. MUST GET IT FIRST.
                //System.out.println(rec.get("m").get("handleName"));
                String handleName = rec.get("m").get("handleName").toString();
                following.add(handleName.substring(1, handleName.length()-1));
                //System.out.println(rec.get("properties").get("handleName").toString());
            }
            return following;
        }catch (Exception ex) {
            ex.printStackTrace();
            return following;
        }
    }

    private List<String> getAllNotFollowing(String handlename){
        List<Record> recordStream;
        List<String> following = new ArrayList<>();
        try(Session session = driver.session()){
            Result result = session.run("MATCH (n:Person {handleName: '"+handlename+"'}) " +
                    "MATCH (p:Person) " +
                    "WHERE NOT (n)-[:FOLLOWS]->(p) AND NOT p.handleName = n.handleName " +
                    "RETURN p");
            recordStream = result.stream().collect(Collectors.toList());
            for (Record rec: recordStream) {
                // rec.get(m) BECAUSE WE WE RETURN M IN CYPHER THIS IS THE RECORD'S NAME. MUST GET IT FIRST.
                //System.out.println(rec.get("m").get("handleName"));
                String handleName = rec.get("p").get("handleName").toString();
                following.add(handleName.substring(1, handleName.length()-1));
                //System.out.println(rec.get("properties").get("handleName").toString());
            }

            return following;
        }catch (Exception ex) {
            ex.printStackTrace();
            return following;
        }

    }

}
