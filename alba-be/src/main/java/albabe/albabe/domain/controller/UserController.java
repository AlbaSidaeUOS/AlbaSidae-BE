package albabe.albabe.domain.controller;

import albabe.albabe.domain.entity.UserEntity;
import albabe.albabe.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        userService.registerUser(username, password, role);
    }

    @PostMapping("/login")
    public UserEntity login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }
}

