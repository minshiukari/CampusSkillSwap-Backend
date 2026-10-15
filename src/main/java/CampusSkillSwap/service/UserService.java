package CampusSkillSwap.service;

import CampusSkillSwap.model.User;
import CampusSkillSwap.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(User user) {

        if (user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()
                || user.getDepartment() == null || user.getDepartment().isBlank()
                || user.getClassYear() == null || user.getClassYear().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "All registration fields are required."
            );
        }

        String email = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with this email already exists."
            );
        }

        user.setName(user.getName().trim());
        user.setEmail(email);
        user.setDepartment(user.getDepartment().trim());
        user.setSkillToTeach("");
        user.setSkillToLearn("");

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found."
                ));
    }

    @Transactional
    public User updateSkills(
            Long id,
            String skillToTeach,
            String skillToLearn
    ) {

        User user = getUserById(id);

        if (skillToTeach == null || skillToTeach.isBlank()
                || skillToLearn == null || skillToLearn.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both skills are required."
            );
        }

        user.setSkillToTeach(skillToTeach.trim());
        user.setSkillToLearn(skillToLearn.trim());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {

        User user = getUserById(id);
        userRepository.delete(user);
    }
}