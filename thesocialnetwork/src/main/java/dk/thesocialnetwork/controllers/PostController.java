package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.CreatePostDTO;
import dk.thesocialnetwork.dto.LikedPostDTO;
import dk.thesocialnetwork.repository.PostRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {


    private PostRepository postRepository;

    private final Driver driver;
    private ArrayList<String> taggedPeople;

    public PostController(PostRepository postRepository, Driver driver) {
        this.postRepository = postRepository;
        this.driver = driver;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreatePostDTO> createPost(@RequestBody CreatePostDTO createPostDTO) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '" + createPostDTO.getAuthor() + "'}) " +
                    "CREATE (p:Post {text: '" + createPostDTO.getText() + "', timeStamp: '" + LocalDateTime.now() + "'}) " +
                    "CREATE (n)-[: CREATED_POST]->(p)" +
                    "RETURN id(p) AS post_id");
            Record record = result.single();
            String id = record.get("post_id").toString();
            for (String taggedPerson : createPostDTO.getTaggedPeople()) {
                session.run("MATCH (" + taggedPerson + ":Person {handleName: '" + taggedPerson + "'}) " +
                        "MATCH (p:Post) " +
                        "WHERE ID(p) = " + id + " " +
                        "CREATE (" + taggedPerson + ")-[: TAGGED_IN]->(p) ");
            }
            return new ResponseEntity(createPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(createPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/like", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LikedPostDTO> likePost(@RequestBody LikedPostDTO likedPostDTO) {
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '" + likedPostDTO.getLikedPerson() + "'})-[r:LIKED]->(p:Post) " +
                    "WHERE ID(p) = " + likedPostDTO.getPostId() + " " +
                    "DELETE r ");
            session.run("MATCH (p:Post) " +
                    "WHERE ID(p) = " + likedPostDTO.getPostId() + " " +
                    "MATCH (n:Person {handleName: '" + likedPostDTO.getLikedPerson() + "'}) " +
                    "CREATE (n)-[: LIKED]->(p) ");
            return new ResponseEntity(likedPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(likedPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/unlike", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LikedPostDTO> unlikePost(@RequestBody LikedPostDTO likedPostDTO) {
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '" + likedPostDTO.getLikedPerson() + "'})-[r:LIKED]->(p:Post) " +
                    "WHERE ID(p) = " + likedPostDTO.getPostId() + " " +
                    "DELETE r ");
            return new ResponseEntity(likedPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(likedPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{handlename}")
    public ResponseEntity getAllPostsPerson(@PathVariable String handlename) {
        List<Record> recordStream;
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: 'Amy'})-[:CREATED_POST]->(Post)" +
                    "RETURN ID(Post), Post.text, Post.timeStamp, n.handleName ");
            recordStream = result.stream().collect(Collectors.toList());

        }
        return new ResponseEntity<>(recordStream.toString(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public void createPostsSample() {
        try (Session session = driver.session()) {
            List<String> taggedPeople = new ArrayList<>();
            taggedPeople.add("Pernille");
            createPost(new CreatePostDTO("Lorem Impsum", "Bob", taggedPeople));
            createPost(new CreatePostDTO("LasdadasdasAdadadsad", "Pernille", taggedPeople));
            taggedPeople.add("Bob");
            createPost(new CreatePostDTO("Dette er en Post", "Dan", taggedPeople));
            createPost(new CreatePostDTO("Whats this youve said to me, my good friend", "Svend", taggedPeople));
            createPost(new CreatePostDTO("Hej med dig", "Eve", taggedPeople));
            taggedPeople.add("Eve");
            createPost(new CreatePostDTO("Hej med dig", "Dan", taggedPeople));
        }
    }


}
