package sanotesoauth2server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;


@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class SanotesOauth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanotesOauth2ServerApplication.class, args);
    }


}
