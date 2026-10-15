package CampusSkillSwap.controller;

import CampusSkillSwap.model.User;
import CampusSkillSwap.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/learn/{userId}")
    public ResponseEntity<?> getStudentsWhoCanTeach(
            @PathVariable Long userId) {
        try {
            List<User> students =
                    matchService.getStudentsWhoCanTeach(userId);

            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/teach/{userId}")
    public ResponseEntity<?> getStudentsWhoWantToLearn(
            @PathVariable Long userId) {
        try {
            List<User> students =
                    matchService.getStudentsWhoWantToLearn(userId);

            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", exception.getMessage()));
        }
    }
}