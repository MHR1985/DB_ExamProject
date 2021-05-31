package dk.thesocialnetwork;

import dk.thesocialnetwork.controllers.LoginController;
import dk.thesocialnetwork.controllers.PersonController;
import dk.thesocialnetwork.dto.FollowsDTO;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.PersonRepository;
import dk.thesocialnetwork.repository.PostRepository;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

import static dk.thesocialnetwork.util.DummyData.createStartdata;


//@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork.db4neo"})
@SpringBootApplication(scanBasePackages = {"dk.thesocialnetwork"})
@EnableNeo4jRepositories("dk.thesocialnetwork.repository")
public class TheSocialNetworkApplication {




    public static void main(String[] args)  {
        SpringApplication.run(TheSocialNetworkApplication.class, args);
        createStartdata();
    }


}
