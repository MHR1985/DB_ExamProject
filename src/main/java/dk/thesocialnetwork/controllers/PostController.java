package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.CreatePostDTO;
import dk.thesocialnetwork.dto.LikedPostDTO;
import dk.thesocialnetwork.dto.PostDTO;
import dk.thesocialnetwork.repository.PostRepository;
import dk.thesocialnetwork.util.HelperUtil;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
@CrossOrigin
public class PostController {
    private PostRepository postRepository;
    private final Driver driver;

    public PostController(Driver driver) {
        this.driver = driver;
        postRepository = new PostRepository(driver);
    }

    @GetMapping("/create")
    public String getNewPostView() {
        return "newpost";
    }

    @GetMapping("")
    public String getNewsFeed(Model model) {
        String author = HelperUtil.getUsernameFromLoggedIn();
        List<PostDTO> postsFromFollowes = postRepository.getPostsFromFollowes(author);
        List<PostDTO> postsFromUser = postRepository.getPostsByUsername(author);
        model.addAttribute("followerPosts", postsFromFollowes);
        model.addAttribute("userPosts",postsFromUser);
        return "newsfeed";
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreatePostDTO> createPost(@RequestBody CreatePostDTO post) {
        try {
            String author = HelperUtil.getUsernameFromLoggedIn();
            String id = postRepository.createPost(post.getText(), author);
            List<String> taggedPeople = getTaggedPeople(post.getText());
            List<String> taggedPeopleFiltered = postRepository.findTaggedPeople(taggedPeople);
            postRepository.addTaggedPeople(id, taggedPeopleFiltered);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> getTaggedPeople(String text) {
        List<String> tags = new ArrayList<>();
        for(String st : text.split(" ")){
            if(st.startsWith("@")){
                tags.add(st.substring(1));
            }
        }
        return tags;
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
}
