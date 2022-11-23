package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail());
    }

    public User toUser(UserDto userDto) {
        return User.Builder.builder().withId(userDto.getId()).withUsername(userDto.getUsername())
                .withFirstname(userDto.getFirstname()).withLastname(userDto.getLastname()).withEmail(userDto.getEmail())
                .build();
    }
}
