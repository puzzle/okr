package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.NewUserDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.dto.userOkrData.UserOkrDataDto;
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The users to create", required = true) @RequestBody List<NewUserDto> newUserDtoList) {
        var createdUsers = this.userAuthorizationService.createUsers(userMapper.toUserList(newUserDtoList));
        return userMapper.toDtos(createdUsers);
    }

    // TODO remove endpoint (no longer used in frontend)
    @Operation(summary = "Check User has KeyResults", description = "Check if User is the owner of KeyResults.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "true if user is owner of KeyResults", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }), })

    @GetMapping(path = "/{id}/iskeyresultowner")
    public Boolean isUserOwnerOfKeyResults(
            @Parameter(description = "The ID for requested user.", required = true) @PathVariable long id) {

        return this.userAuthorizationService.isUserOwnerOfKeyResults(id);
    }

    @Operation(summary = "Check if User is member of Teams", description = "Check if User is member of any Team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "true if user is member of a Team", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }), })

    @GetMapping(path = "/{id}/ismemberofteams")
    public Boolean isUserMemberOfTeams(
            @Parameter(description = "The ID of the user.", required = true) @PathVariable long id) {

        return this.userAuthorizationService.isUserMemberOfTeams(id);
    }

    @Operation(summary = "Get User OKR Data", description = "Get User OKR Data")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned User OKR Data.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserOkrDataDto.class)) }), })
    @GetMapping(path = "/{id}/userokrdata")
    public UserOkrDataDto getUserOkrData(@PathVariable long id) {
        return this.userAuthorizationService.getUserOkrData(id);
    }

    @Operation(summary = "Delete User by Id", description = "Delete User by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted User by Id"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete a User", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the User with requested id") })
    @DeleteMapping(path = "/{id}")
    public void deleteUserById(@PathVariable long id) {
        this.userAuthorizationService.deleteEntityById(id);
    }

}
