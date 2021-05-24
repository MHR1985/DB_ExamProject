package dk.thesocialnetwork.repository;


import dk.thesocialnetwork.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository extends Neo4jRepository<Person, String> {

    Person getPersonByHandleName(String handleName);

}
