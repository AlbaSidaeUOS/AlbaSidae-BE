package albabe.albabe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://6211-175-193-73-151.ngrok-free.app")})
@SpringBootApplication
public class AlbaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbaBeApplication.class, args);
    }

}
