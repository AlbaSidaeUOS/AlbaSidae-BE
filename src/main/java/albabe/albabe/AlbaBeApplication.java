package albabe.albabe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    Description : 프로그램 실행 소스파일
 */

//@OpenAPIDefinition(servers = {@Server(url = "")})
@SpringBootApplication
public class AlbaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbaBeApplication.class, args);
    }

}
