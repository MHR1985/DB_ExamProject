package dk.thesocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

//@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork.db4neo"})
@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork"})
@EnableNeo4jRepositories("dk.thesocialnetwork.repository")
public class TheSocialNetworkApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(TheSocialNetworkApplication.class, args);
//        ChatApplication.run();
    }

}
