package dk.thesocialnetwork.util;

import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.repository.PersonRepository;
import dk.thesocialnetwork.repository.PostRepository;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyData {

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
        postRepository.createPost("hej @frederik og @martin", "kenneth");
        postRepository.createPost("@martin har købt den sejeste bil", "kenneth");
        postRepository.createPost("Hej alle sammen", "kenneth");
        postRepository.createPost("hej @simon og @frederik", "martin");
        postRepository.createPost("Godt gået", "martin");
        postRepository.createPost("hej @kenneth og @martin", "simon");
        postRepository.createPost("Det styrer for vildt", "simon");
        postRepository.createPost("Hej med jer!", "frederik");
        postRepository.createPost("Hej @simon", "frederik");
    }

    @Autowired
    public void setDriverStatic() {
        this.driverStatic = driver;
    }
}
