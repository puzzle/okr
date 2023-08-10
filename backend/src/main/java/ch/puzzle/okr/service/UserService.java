package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ValidationService validationService;

    public UserService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getOwnerById(Long ownerId) {
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute owner id");
        }

        return userRepository.findById(ownerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Owner with id %d not found", ownerId))));
    }

    @Cacheable(value = "users", key = "#newUser.username")
    public synchronized User getOrCreateUser(User newUser) {
        Optional<User> user = userRepository.findByUsername(newUser.getUsername());
        return user.orElseGet(() -> {
            validationService.validateOnSave(newUser);
            return userRepository.save(newUser);
        });
    }
}
