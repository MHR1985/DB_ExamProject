package dk.thesocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import static dk.thesocialnetwork.util.DummyData.createStartdata;


@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork"})
@EnableNeo4jRepositories("dk.thesocialnetwork.repository")
public class TheSocialNetworkApplication {


    public static void main(String[] args) {
        SpringApplication.run(TheSocialNetworkApplication.class, args);
        createStartdata();
    }


}
