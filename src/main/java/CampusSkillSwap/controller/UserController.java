package CampusSkillSwap.controller;

import CampusSkillSwap.model.User;
import CampusSkillSwap.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedUser);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(
                    userService.getUserById(userId)
            );
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @PutMapping("/{userId}/skills")
    public ResponseEntity<?> updateSkills(
            @PathVariable Long userId,
            @RequestBody Map<String, String> skills) {
        try {
            User updatedUser = userService.updateSkills(
                    userId,
                    skills.get("skillToLearn"),
                    skills.get("skillToTeach")
            );

            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId) {
        try {
            userService.deleteUser(userId);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }
}