package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.service.authorization.UserAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserAuthorizationService userAuthorizationService;
    private final AuthorizationService authorizationService;
    private final UserMapper userMapper;

    public UserController(UserAuthorizationService userAuthorizationService, AuthorizationService authorizationService,
            UserMapper userMapper) {
        this.userAuthorizationService = userAuthorizationService;
        this.authorizationService = authorizationService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get Users", description = "Get all Users from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Users.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userAuthorizationService.getAllUsers().stream().map(userMapper::toDto).toList();
    }

    @Operation(summary = "Get Current User", description = "Get all current logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned current logged in user.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @GetMapping(path = "/current")
    public UserDto getCurrentUser() {
        var currentUser = this.authorizationService.getAuthorizationUser().user();
        return userMapper.toDto(currentUser);
    }

}
