package dk.thesocialnetwork.util;

import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.repository.PersonRepository;
import dk.thesocialnetwork.repository.PostRepository;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DummyDataCypher {

    @Autowired
    Driver driver;

    static Driver driverStatic;

    public static void createStartdata() {
        PersonRepository personRepository = new PersonRepository(driverStatic);
        if (personRepository.checkIfDataInDatabase())
            return;
        createDummyUsersAndFollowers();
        createDummyPosts();


    }


    private static void createDummyUsersAndFollowers() {
        PersonRepository personRepository = new PersonRepository(driverStatic);

        personRepository.createPerson("kenneth");
        personRepository.createPerson("simon");
        personRepository.createPerson("martin");
        personRepository.createPerson("frederik");

        personRepository.createFollower(new FollowsDTO("kenneth", "simon"));
        personRepository.createFollower(new FollowsDTO("kenneth", "frederik"));
        personRepository.createFollower(new FollowsDTO("simon", "martin"));
        personRepository.createFollower(new FollowsDTO("simon", "frederik"));
        personRepository.createFollower(new FollowsDTO("martin", "kenneth"));
        personRepository.createFollower(new FollowsDTO("martin", "frederik"));
        personRepository.createFollower(new FollowsDTO("frederik", "kenneth"));
        personRepository.createFollower(new FollowsDTO("frederik", "simon"));
    }

    private static void createDummyPosts() {
        PostRepository postRepository = new PostRepository(driverStatic);

        String post1 = postRepository.createPost("hej @frederik og @martin", "kenneth");
        ArrayList<String> taggedPeople = new ArrayList<>();
        taggedPeople.add("frederik");
        taggedPeople.add("martin");
        postRepository.addTaggedPeople(post1,taggedPeople);
        String post2 = postRepository.createPost("@martin har købt den sejeste bil", "kenneth");
        taggedPeople = new ArrayList<>();
        taggedPeople.add("martin");
        postRepository.addTaggedPeople(post2,taggedPeople);
        postRepository.createPost("Hej alle sammen", "kenneth");
        String post3 = postRepository.createPost("hej @simon og @frederik", "martin");
        taggedPeople = new ArrayList<>();
        taggedPeople.add("simon");
        taggedPeople.add("frederik");
        postRepository.addTaggedPeople(post3,taggedPeople);
        postRepository.createPost("Godt gået", "martin");
        String post4 = postRepository.createPost("hej @kenneth og @martin", "simon");
        taggedPeople = new ArrayList<>();
        taggedPeople.add("kenneth");
        taggedPeople.add("martin");
        postRepository.addTaggedPeople(post4,taggedPeople);
        postRepository.createPost("Det styrer for vildt", "simon");
        postRepository.createPost("Hej med jer!", "frederik");
        String post5 = postRepository.createPost("Hej @simon", "frederik");
        taggedPeople = new ArrayList<>();
        taggedPeople.add("simon");
        postRepository.addTaggedPeople(post5,taggedPeople);
    }

    @Autowired
    public void setDriverStatic() {
        this.driverStatic = driver;
    }
}
