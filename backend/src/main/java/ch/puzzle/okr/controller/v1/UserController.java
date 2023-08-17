package ch.puzzle.okr.controller.v1;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final RegisterNewUserService registerNewUserService;
    private final UserMapper userMapper;

    public UserController(UserService userService, RegisterNewUserService registerNewUserService,
            UserMapper userMapper) {
        this.userService = userService;
        this.registerNewUserService = registerNewUserService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get Users", description = "Get all Users from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Users.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @GetMapping
    public List<UserDto> getAllUsers() {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return userService.getAllUsers().stream().map(userMapper::toDto).toList();
    }

}
