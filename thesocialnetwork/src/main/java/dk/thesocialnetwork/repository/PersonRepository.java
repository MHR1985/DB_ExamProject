package dk.thesocialnetwork.repository;


import dk.thesocialnetwork.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

//Conert this interface to class, and use that method in LoginController.createUser(). Also create method that gets all users you do not follow and conert to JSON if necessary
public interface PersonRepository extends Neo4jRepository<Person, String> {

    Person getPersonByHandleName(String handleName);

}
