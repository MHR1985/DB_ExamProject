package dk.thesocialnetwork;

import dk.thesocialnetwork.util.DummyDataCypher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork"})
@EnableNeo4jRepositories("dk.thesocialnetwork.repository")
public class TheSocialNetworkApplication {


    public static void main(String[] args) {
        SpringApplication.run(TheSocialNetworkApplication.class, args);
        DummyDataCypher.createStartdata();
        RedisChatExample.createChats();
    }





}
