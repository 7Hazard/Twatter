package solutions.desati.twatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwatterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwatterApplication.class, args);

//        SpringApplication application = new SpringApplication(TwatterApplication.class);
//        application.setAdditionalProfiles("ssl");
//        application.run(args);
    }

}
