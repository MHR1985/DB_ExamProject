package dk.thesocialnetwork.repository;

import dk.thesocialnetwork.model.Person;
import dk.thesocialnetwork.model.Post;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRepository {

    Driver driver;

    public PostRepository(Driver driver) {
        this.driver = driver;
    }

    public String createPost(String text, String author) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '" + author + "'}) " +
                    "CREATE (p:Post {text: '" + text + "', timeStamp: '" + LocalDateTime.now() + "'}) " +
                    "CREATE (n)-[: CREATED_POST]->(p)" +
                    "RETURN id(p) AS post_id");
            Record record = result.single();
            String id = record.get("post_id").toString();
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void addTaggedPeople(String post_id, List<String> taggedPeople) {
        try (Session session = driver.session()) {
            for (String taggedPerson : taggedPeople) {
                session.run("MATCH (" + taggedPerson + ":Person {handleName: '" + taggedPerson + "'}) " +
                        "MATCH (p:Post) " +
                        "WHERE ID(p) = " + post_id + " " +
                        "CREATE (" + taggedPerson + ")-[: TAGGED_IN]->(p) ");
            }
        }
    }

    public List<String> findTaggedPeople(List<String> taggedPeople) {
        List<String> foundPeople = new ArrayList<>();
        List<Record> recordStream = new ArrayList<>();
        String build = "WITH [";
        try (Session session = driver.session()) {
            for (int i = 0; i <= taggedPeople.size() - 1; i++) {
                if (i == taggedPeople.size() - 1) {
                    build += "'" + taggedPeople.get(i) + "'";
                } else {
                    build += "'" + taggedPeople.get(i) + "', ";
                }
            }
            build += "] AS handleNames " +
                    "UNWIND handleNames AS handleName " +
                    "MATCH (p:Person {handleName: handleName}) " +
                    "RETURN p";
            Result result = session.run(build);
            recordStream = result.stream().collect(Collectors.toList());
            for (Record rec : recordStream) {
                String handleName = rec.get("p").get("handleName").toString();
                foundPeople.add(handleName.substring(1, handleName.length() - 1));
            }
            return foundPeople;
        }
    }

    public List<Post> getPostsFromFollowes(String username) {
        List<Record> recordStream;
        List<Post> follows = new ArrayList<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '"+username+"'}) " +
                    "MATCH (n)-[r:FOLLOWS]->(m) " +
                    "MATCH (m)-[:CREATED_POST]->(p) " +
                    "OPTIONAL MATCH (v)-[t:TAGGED_IN]->(p) " +
                    "return p, m, id(p) AS post_id, collect(v) as v " +
                    "ORDER BY p.timeStamp DESC");
            recordStream = result.stream().collect(Collectors.toList());
            for (Record rec : recordStream) {
                String id = rec.get("post_id").toString();
                List<Person> tags = new ArrayList<>();
                for(Value val: rec.get("v").values()){
                    String taggedPerson = val.get("handleName").toString();
                    taggedPerson = taggedPerson.substring(1, taggedPerson.length() - 1);
                    tags.add(new Person(taggedPerson));
                }
                String text = rec.get("p").get("text").toString();
                text = text.substring(1, text.length() - 1);
                String timestamp = rec.get("p").get("timeStamp").toString();
                timestamp = timestamp.substring(1, timestamp.length() - 1);
                String author = rec.get("m").get("handleName").toString();
                author  = author.substring(1, author.length() - 1);
                Post post = new Post(Long.parseLong(id),text,tags, LocalDateTime.parse(timestamp),new Person(author));
                follows.add(post);
            }
            return follows;
        }
    }

    public List<Post> getPostsByUsername (String username) {
        List<Record> recordStream;
        List<Post> follows = new ArrayList<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '"+username+"'}) " +
                    "MATCH (n)-[:CREATED_POST]->(p) " +
                    "OPTIONAL MATCH (v)-[t:TAGGED_IN]->(p) " +
                    "return p, n, id(p) AS post_id, collect(v) as v " +
                    "ORDER BY p.timeStamp DESC");
            recordStream = result.stream().collect(Collectors.toList());
            for (Record rec : recordStream) {
                String id = rec.get("post_id").toString();
                List<Person> tags = new ArrayList<>();
                for(Value val: rec.get("v").values()){
                    String taggedPerson = val.get("handleName").toString();
                    taggedPerson = taggedPerson.substring(1, taggedPerson.length() - 1);
                    tags.add(new Person(taggedPerson));
                }
                String text = rec.get("p").get("text").toString();
                text = text.substring(1, text.length() - 1);
                String timestamp = rec.get("p").get("timeStamp").toString();
                timestamp = timestamp.substring(1, timestamp.length() - 1);
                String author = rec.get("n").get("handleName").toString();
                author  = author.substring(1, author.length() - 1);
                Post post = new Post(Long.parseLong(id),text,tags, LocalDateTime.parse(timestamp),new Person(author));
                follows.add(post);
            }
            return follows;
        }
    }
}
