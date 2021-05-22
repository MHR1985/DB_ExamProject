package dk.thesocialnetwork.db4neo.repository;

import dk.thesocialnetwork.db4neo.model.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PostRepository extends Neo4jRepository<Post, Long> {

    @Query("MATCH (n:Person {handleName: '$props'})-[:CREATED_POST]->(Post) RETURN Post")
    Collection<Post> getAllPostsByPerson(@Param("props") String handlename);
}
