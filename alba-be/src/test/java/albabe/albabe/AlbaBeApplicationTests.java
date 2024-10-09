package albabe.albabe;

import albabe.albabe.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlbaBeApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private UserRepository userRepository;

}
