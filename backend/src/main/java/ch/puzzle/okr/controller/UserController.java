package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.NewUserDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.service.authorization.UserAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

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
        var currentUser = this.authorizationService.updateOrAddAuthorizationUser().user();
        return userMapper.toDto(currentUser);
    }

    @Operation(summary = "Get User by ID", description = "Get user by given ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned user", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @GetMapping(path = "/{id}")
    public UserDto getUserById(
            @Parameter(description = "The ID for requested user.", required = true) @PathVariable long id) {
        var user = this.userAuthorizationService.getById(id);
        return userMapper.toDto(user);
    }

    @Operation(summary = "Set OKR Champion property for user", description = "Sets the property okrChampion of user to true or false")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned user", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @PutMapping(path = "/{id}/isokrchampion/{isOkrChampion}")
    public UserDto setOkrChampion(
            @Parameter(description = "The ID for requested user.", required = true) @PathVariable long id,
            @Parameter(description = "okrChampion property of user is set to this flag.", required = true) @PathVariable boolean isOkrChampion) {
        var user = this.userAuthorizationService.setIsOkrChampion(id, isOkrChampion);
        return userMapper.toDto(user);
    }

    @Operation(summary = "Create users", description = "Creates a user entity for every user in the method body")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned users", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }), })
    @PostMapping(path = "/createall")
    public List<UserDto> createUsers(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Team as json to create a new Team.", required = true) @RequestBody List<NewUserDto> newUserDtoList) {
        var createdUsers = this.userAuthorizationService.createUsers(userMapper.toUserList(newUserDtoList));
        return userMapper.toDtos(createdUsers);
    }

}
