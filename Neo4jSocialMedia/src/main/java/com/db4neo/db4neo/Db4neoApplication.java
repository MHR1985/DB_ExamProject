package com.db4neo.db4neo;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication(scanBasePackages = {"com.db4neo.db4neo"})
@EnableNeo4jRepositories("com.db4neo.db4neo.repository")
public class Db4neoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Db4neoApplication.class, args);

    }

}
