package albabe.albabe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@OpenAPIDefinition(servers = {@Server(url = "")})
@SpringBootApplication
public class AlbaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbaBeApplication.class, args);
    }

}
