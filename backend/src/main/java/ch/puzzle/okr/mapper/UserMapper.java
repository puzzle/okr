package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.NewUserDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.dto.UserTeamDto;
import ch.puzzle.okr.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final TeamMapper teamMapper;

    public UserMapper(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    public List<UserDto> toDtos(List<User> userList) {
        return userList.stream().map(this::toDto).toList();
    }

    public UserDto toDto(User user) {
        var userTeams = user.getUserTeamList().stream().map(
                ut -> new UserTeamDto(ut.getId(), user.getVersion(), teamMapper.toDto(ut.getTeam()), ut.isTeamAdmin()))
                .collect(Collectors.toList());

        return new UserDto(user.getId(), user.getVersion(), user.getFirstname(), user.getLastname(), user.getEmail(),
                userTeams, user.isOkrChampion());
    }

    public List<User> toUserList(List<NewUserDto> newUserList) {
        return newUserList.stream().map(this::toUser).toList();
    }

    public User toUser(NewUserDto newUserDto) {
        var user = new User();
        user.setFirstname(newUserDto.firstname());
        user.setLastname(newUserDto.lastname());
        user.setEmail(newUserDto.email());
        return user;
    }
}
