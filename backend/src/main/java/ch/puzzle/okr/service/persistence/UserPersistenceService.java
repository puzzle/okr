package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserPersistenceService {

    private final UserRepository userRepository;

    public UserPersistenceService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public synchronized User getOrCreateUser(User newUser) {
        Optional<User> user = userRepository.findByUsername(newUser.getUsername());
        return user.orElseGet(() -> userRepository.save(newUser));
    }

    void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
