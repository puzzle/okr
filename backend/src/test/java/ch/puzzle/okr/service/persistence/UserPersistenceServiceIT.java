package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static ch.puzzle.okr.Constants.USER;
import static ch.puzzle.okr.util.CollectionUtils.iterableToList;
import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class UserPersistenceServiceIT {

    private User createdUser;

    @Autowired
    private UserPersistenceService userPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            userPersistenceService.deleteById(createdUser.getId());
            createdUser = null;
        }
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("should save user on save() with empty user team list")
    @Test
    void saveShouldSaveUserWithEmptyUserTeamList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstName("Hans") //
                .withLastName("Muster") //
                .withEmail("muster@puzzle.ch") //
                .withUserTeamList(List.of()).build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("should save user with null value for user team list on save()")
    @Test
    void saveShouldSaveUserWithNullUserTeamList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstName("Hans") //
                .withLastName("Muster") //
                .withEmail("muster@puzzle.ch") //
                .withUserTeamList(null).build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("should save all users in the input list on saveAll()")
    @Test
    void saveAllShouldSaveAllUsersInTheInputList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstName("Hans") //
                .withLastName("Muster") //
                .withEmail("muster@puzzle.ch") //
                .build();

        // act
        var createdUsers = iterableToList(userPersistenceService.saveAll(List.of(newUser)));

        // assert
        assertEquals(1, createdUsers.size());
        createdUser = createdUsers.getFirst();

        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("should return single user on getOrCreateUser() when user found")
    @Test
    void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
        // arrange
        var existingUser = User.Builder.builder().withEmail("wunderland@puzzle.ch").build();

        // act
        var returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        // assert
        assertUser(11L, "Alice", "Wunderland", "wunderland@puzzle.ch", returnedUser);
    }

    @DisplayName("should return saved user on getOrCreateUser() when user not found")
    @Test
    void getOrCreateUserShouldReturnSavedUserWhenUserNotFound() {
        // arrange
        var newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstName("firstname") //
                .withLastName("lastname") //
                .withEmail("lastname@puzzle.ch") //
                .build();

        // act
        createdUser = userPersistenceService.getOrCreateUser(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("firstname", "lastname", "lastname@puzzle.ch", createdUser);
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("should return user on findByEmail() when email is found")
    @Test
    void findByEmailShouldReturnUserIfEmailIsFound() {
        Optional<User> user = userPersistenceService.findByEmail("gl@gl.com");

        assertTrue(user.isPresent());
        assertEquals("Jaya", user.get().getFirstName());
        assertEquals("Norris", user.get().getLastName());
    }

    @DisplayName("should return empty optional on findByEmail() when email is not found")
    @Test
    void findByEmailShouldReturnEmptyOptionalIfEmailIsNotFound() {
        assertTrue(userPersistenceService.findByEmail("not_valid@gl.com").isEmpty());
    }

    @DisplayName("should return empty optional on findByEmail() when email is null")
    @Test
    void findByEmailShouldReturnEmptyOptionalIfEmailIsNull() {
        assertTrue(userPersistenceService.findByEmail(null).isEmpty());
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("should return all okr champions on findAllOkrChampions()")
    @Test
    void findAllOkrChampionsShouldReturnAllOkrChampions() {
        // act
        var allOkrChampions = userPersistenceService.findAllOkrChampions();

        // assert
        assertEquals(1, allOkrChampions.size());
        assertUser(61L, "Jaya", "Norris", "gl@gl.com", allOkrChampions.getFirst());
    }

    @DisplayName("should return user on getModelName()")
    @Test
    void getModelNameShouldReturnUser() {
        assertEquals(USER, userPersistenceService.getModelName());
    }

    private void assertUser(Long id, String firstName, String lastName, String email, User currentUser) {
        assertEquals(id, currentUser.getId());
        assertUser(firstName, lastName, email, currentUser);
    }

    private void assertUser(String firstName, String lastName, String email, User currentUser) {
        assertEquals(firstName, currentUser.getFirstName());
        assertEquals(lastName, currentUser.getLastName());
        assertEquals(email, currentUser.getEmail());
    }

    @DisplayName("should delete user on deleteById() when user exists")
    @Test
    void deleteByIdShouldDeleteUserWhenUserExists() {
        // arrange
        User user = createUser();

        // act
        userPersistenceService.deleteById(user.getId());

        // assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class, //
                () -> userPersistenceService.findById(createdUser.getId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    private User createUser() {
        User newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstName("firstname") //
                .withLastName("lastname") //
                .withEmail("lastname@puzzle.ch") //
                .build();
        createdUser = userPersistenceService.getOrCreateUser(newUser);
        assertNotNull(createdUser.getId());
        return createdUser;
    }

    @DisplayName("should throw exception on deleteById() when Id is null")
    @Test
    void deleteByIdShouldThrowExceptionWhenIdIsNull() {
        InvalidDataAccessApiUsageException exception = assertThrows(InvalidDataAccessApiUsageException.class, //
                () -> userPersistenceService.deleteById(null));

        assertEquals("The given id must not be null", exception.getMessage());
    }
}