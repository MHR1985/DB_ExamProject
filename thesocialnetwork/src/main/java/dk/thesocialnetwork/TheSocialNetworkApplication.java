package dk.thesocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

//@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork.db4neo"})
@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork"})
@EnableNeo4jRepositories("dk.thesocialnetwork.repository")
public class TheSocialNetworkApplication {


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(TheSocialNetworkApplication.class, args);
//        ChatApplication.run();

    }

}
