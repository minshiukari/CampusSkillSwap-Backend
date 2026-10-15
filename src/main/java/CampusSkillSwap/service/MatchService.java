package CampusSkillSwap.service;

import CampusSkillSwap.model.User;
import CampusSkillSwap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class MatchService {

    private final UserRepository userRepository;

    public MatchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getStudentsWhoCanTeach(Long userId) {
        User currentUser = getCurrentUser(userId);

        String desiredSkill = normalize(
                currentUser.getSkillToLearn()
        );

        if (desiredSkill.isEmpty()) {
            return List.of();
        }

        return userRepository.findAll()
                .stream()
                .filter(student ->
                        !student.getId().equals(userId)
                )
                .filter(student ->
                        normalize(student.getSkillToTeach())
                                .equals(desiredSkill)
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> getStudentsWhoWantToLearn(Long userId) {
        User currentUser = getCurrentUser(userId);

        String offeredSkill = normalize(
                currentUser.getSkillToTeach()
        );

        if (offeredSkill.isEmpty()) {
            return List.of();
        }

        return userRepository.findAll()
                .stream()
                .filter(student ->
                        !student.getId().equals(userId)
                )
                .filter(student ->
                        normalize(student.getSkillToLearn())
                                .equals(offeredSkill)
                )
                .toList();
    }

    private User getCurrentUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found.")
                );
    }

    private String normalize(String skill) {
        return skill == null
                ? ""
                : skill.trim().toLowerCase(Locale.ROOT);
    }
}