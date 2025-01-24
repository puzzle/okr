package ch.puzzle.okr.service.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.WithMockAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringIntegrationTest
class UnitPersistenceServiceIT {

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @DisplayName("Should save user on save() with empty user team list")
    @Test
    @WithMockAuthUser
    void saveShouldSaveUserWithEmptyUserTeamList() {
        // arrange
        var newUnit = Unit.Builder.builder().unitName("Muster").build();

        // act
        var createdUnit = unitPersistenceService.save(newUnit);

        // assert
        assertNotNull(createdUnit.getId());
        assertUser("Mocked", "User", "gl@gl.com", createdUnit.getCreatedBy());
    }

    // @DisplayName("Should save user with null value for user team list on save()")
    // @Test
    // void saveShouldSaveUserWithNullUserTeamList() {
    // // arrange
    // var newUser = User.Builder
    // .builder() //
    // .withFirstName("Hans") //
    // .withLastName("Muster") //
    // .withEmail("muster@puzzle.ch") //
    // .withUserTeamList(null)
    // .build();
    //
    // // act
    // createdUser = unitP.save(newUser);
    //
    // // assert
    // assertNotNull(createdUser.getId());
    // assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    // }
    //
    // @DisplayName("Should save all users in the input list on saveAll()")
    // @Test
    // void saveAllShouldSaveAllUsersInTheInputList() {
    // // arrange
    // var newUser = User.Builder
    // .builder() //
    // .withFirstName("Hans") //
    // .withLastName("Muster") //
    // .withEmail("muster@puzzle.ch") //
    // .build();
    //
    // // act
    // var createdUsers = iterableToList(unitP.saveAll(List.of(newUser)));
    //
    // // assert
    // assertEquals(1, createdUsers.size());
    // createdUser = createdUsers.getFirst();
    //
    // assertNotNull(createdUser.getId());
    // assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    // }
    //
    // @DisplayName("Should return single user on getOrCreateUser() when user
    // found")
    // @Test
    // void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
    // // arrange
    // var existingUser =
    // User.Builder.builder().withEmail("wunderland@puzzle.ch").build();
    //
    // // act
    // var returnedUser = unitP.getOrCreateUser(existingUser);
    //
    // // assert
    // assertUser(11L, "Alice", "Wunderland", "wunderland@puzzle.ch", returnedUser);
    // }
    //
    // @DisplayName("Should return saved user on getOrCreateUser() when user not
    // found")
    // @Test
    // void getOrCreateUserShouldReturnSavedUserWhenUserNotFound() {
    // // arrange
    // var newUser = User.Builder
    // .builder() //
    // .withId(null) //
    // .withFirstName("firstname") //
    // .withLastName("lastname") //
    // .withEmail("lastname@puzzle.ch") //
    // .build();
    //
    // // act
    // createdUser = unitP.getOrCreateUser(newUser);
    //
    // // assert
    // assertNotNull(createdUser.getId());
    // assertUser("firstname", "lastname", "lastname@puzzle.ch", createdUser);
    // }
    //
    // // uses data from V100_0_0__TestData.sql
    // @DisplayName("Should return user on findByEmail() when email is found")
    // @Test
    // void findByEmailShouldReturnUserIfEmailIsFound() {
    // Optional<User> user = unitP.findByEmail("gl@gl.com");
    //
    // assertTrue(user.isPresent());
    // assertEquals("Jaya", user.get().getFirstName());
    // assertEquals("Norris", user.get().getLastName());
    // }
    //
    // @DisplayName("Should return empty optional on findByEmail() when email is not
    // found")
    // @Test
    // void findByEmailShouldReturnEmptyOptionalIfEmailIsNotFound() {
    // assertTrue(unitP.findByEmail("not_valid@gl.com").isEmpty());
    // }
    //
    // @DisplayName("Should return empty optional on findByEmail() when email is
    // null")
    // @Test
    // void findByEmailShouldReturnEmptyOptionalIfEmailIsNull() {
    // assertTrue(unitP.findByEmail(null).isEmpty());
    // }
    //
    // // uses data from V100_0_0__TestData.sql
    // @DisplayName("Should return all okr champions on findAllOkrChampions()")
    // @Test
    // void findAllOkrChampionsShouldReturnAllOkrChampions() {
    // // act
    // var allOkrChampions = unitP.findAllOkrChampions();
    //
    // // assert
    // assertEquals(1, allOkrChampions.size());
    // assertUser(61L, "Jaya", "Norris", "gl@gl.com", allOkrChampions.getFirst());
    // }
    //
    // @DisplayName("Should return user on getModelName()")
    // @Test
    // void getModelNameShouldReturnUser() {
    // assertEquals(USER, unitP.getModelName());
    // }
    //
    private void assertUser(Long id, String firstName, String lastName, String email, User currentUser) {
        assertEquals(id, currentUser.getId());
        assertUser(firstName, lastName, email, currentUser);
    }

    //
    private void assertUser(String firstName, String lastName, String email, User currentUser) {
        assertEquals(firstName, currentUser.getFirstName());
        assertEquals(lastName, currentUser.getLastName());
        assertEquals(email, currentUser.getEmail());
    }
    //
    // @DisplayName("Should delete user on deleteById() when user exists")
    // @Test
    // void deleteByIdShouldDeleteUserWhenUserExists() {
    // // arrange
    // User user = createUser();
    // Long userId = user.getId();
    //
    // // act
    // unitP.deleteById(user.getId());
    //
    // // assert
    // OkrResponseStatusException exception =
    // assertThrows(OkrResponseStatusException.class,
    // () -> unitP.findById(userId));
    //
    // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    // }
    //
    // private User createUser() {
    // User newUser = User.Builder
    // .builder() //
    // .withId(null) //
    // .withFirstName("firstname") //
    // .withLastName("lastname") //
    // .withEmail("lastname@puzzle.ch") //
    // .build();
    // createdUser = unitP.getOrCreateUser(newUser);
    // assertNotNull(createdUser.getId());
    // return createdUser;
    // }
    //
    // @DisplayName("Should throw exception on deleteById() when Id is null")
    // @Test
    // void deleteByIdShouldThrowExceptionWhenIdIsNull() {
    // InvalidDataAccessApiUsageException exception =
    // assertThrows(InvalidDataAccessApiUsageException.class, //
    // () -> unitP.deleteById(null));
    //
    // assertEquals("The given id must not be null", exception.getMessage());
    // }
}