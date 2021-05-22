package dk.thesocialnetwork.db4neo.repository;


import dk.thesocialnetwork.db4neo.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, String> {

    Person getPersonByHandleName(String handleName);

}
