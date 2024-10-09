package albabe.albabe.domain.controller;

import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserEntity user) {
        userService.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getName(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPhone(),
                user.getBusinessNumber()
        );
    }
}

