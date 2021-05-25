package dk.thesocialnetwork.repository;


import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.model.Person;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Conert this interface to class, and use that method in LoginController.createUser(). Also create method that gets all users you do not follow and conert to JSON if necessary
public class PersonRepository  {
    private final Driver driver;

    public PersonRepository(Driver driver) {
        this.driver = driver;
    }

    public List<String> getAllFollowing(String handlename){
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

    public List<String> getAllNotFollowing(String handlename){
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

    public void createPerson(String username) {
        try (Session session = driver.session()) {
            session.run("CREATE (:Person {handleName: \"" + username + "\"})");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getHandleName(String username) {
        Person person;
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '"+username+"'}) " +
                    "return n.handleName ");
            if (result.hasNext()) {
                String name = result.single().get("n.handleName").toString();
                System.out.println(name);
                return name.substring(1, name.length()-1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFollower(FollowsDTO dto){
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
    public void createFollower(FollowsDTO dto){
        try (Session session = driver.session()){
            session.run("MATCH (n:Person {handleName: '"+ dto.follower + "'}) " +
                    "MATCH (m:Person {handleName: '"+dto.target+"'}) " +
                    "CREATE (n)-[:FOLLOWS]->(m) " +
                    "CREATE (n)<-[:FOLLOWED_BY]-(m)");
        }
    }
}
