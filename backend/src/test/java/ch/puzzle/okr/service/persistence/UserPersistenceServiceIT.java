package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class UserPersistenceServiceIT {

    private static final long ID_ALICE = 11L;
    private static final String EMAIL_ALICE = "wunderland@puzzle.ch";
    private static final String EMAIL_TEST_USER = "lastname@puzzle.ch";

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

    @Test
    void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
        User existingUser = User.Builder.builder().withEmail(EMAIL_ALICE).build();

        User returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        assertEquals(11L, returnedUser.getId());
        assertEquals("Alice", returnedUser.getFirstname());
        assertEquals("Wunderland", returnedUser.getLastname());
        assertEquals("wunderland@puzzle.ch", returnedUser.getEmail());
    }

    // uses test data from V100_0_0__TestData.sql
    @DisplayName("getOrCreateUser() should return saved user when user found")
    @Test
    void getOrCreateUserShouldReturnSavedUserWhenUserFound() {
        // arrange
        var initialNumberOfUsers = userPersistenceService.findAll().size();
        var existingUser = userPersistenceService.findById(ID_ALICE);

        // act
        var returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        // assert
        assertEquals(existingUser, returnedUser);
        assertNoUserCreated(initialNumberOfUsers);
    }

    private void assertNoUserCreated(int initialNumberOfUsers) {
        var finalNumberOfUsers = userPersistenceService.findAll().size();
        assertEquals(initialNumberOfUsers, finalNumberOfUsers);
    }

    @DisplayName("getOrCreateUser() should save user when user not found")
    @Test
    void getOrCreateUserShouldSaveUserWhenUserNotFound() {
        // arrange
        var initialNumberOfUsers = userPersistenceService.findAll().size();
        User newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstname("firstname") //
                .withLastname("lastname") //
                .withEmail(EMAIL_TEST_USER) //
                .build();

        // act
        createdUser = userPersistenceService.getOrCreateUser(newUser);

        // assert§
        assertEquals(newUser, createdUser);
        assertNewUserCreated(initialNumberOfUsers);
    }

    private void assertNewUserCreated(int initialNumberOfUsers) {
        var finalNumberOfUsers = userPersistenceService.findAll().size();
        assertEquals(initialNumberOfUsers + 1, finalNumberOfUsers);
    }

    // uses test data from V100_0_0__TestData.sql
    @DisplayName("findByEmail() should return non empty optional with the user when user is found")
    @Test
    void findByEmailShouldReturnNonEmptyOptionalWithTheUserWhenUserIsFound() {
        // arrange
        User expected = User.Builder.builder() //
                .withId(ID_ALICE) //
                .withVersion(1) //
                .withFirstname("Alice") //
                .withLastname("Wunderland") //
                .withEmail(EMAIL_ALICE) //
                .withOkrChampion(false) //
                .build();

        // act
        Optional<User> foundUser = userPersistenceService.findByEmail(EMAIL_ALICE);

        // assert
        assertTrue(foundUser.isPresent());
        assertEquals(expected, foundUser.get());
    }

    @DisplayName("findByEmail() should return empty optional when user not found")
    @Test
    void findByEmailShouldReturnEmptyOptionalWhenUserNotFound() {
        // act
        Optional<User> foundUser = userPersistenceService.findByEmail("non.existing@email.com");

        // assert
        assertTrue(foundUser.isEmpty());
    }

    // uses test data from V100_0_0__TestData.sql
    @DisplayName("findAllOkrChampions() should return okr champions")
    @Test
    void findAllOkrChampionsShouldReturnOkrChampions() {
        // arrange
        User expected = User.Builder.builder() //
                .withId(61L) //
                .withVersion(1) //
                .withFirstname("Jaya") //
                .withLastname("Norris") //
                .withEmail("gl@gl.com") //
                .withOkrChampion(true) //
                .build();

        // act
        List<User> allOkrChampions = userPersistenceService.findAllOkrChampions();

        // assert
        assertEquals(1, allOkrChampions.size());
        assertEquals(expected, allOkrChampions.get(0));
    }

    @DisplayName("save() should save a single user")
    @Test
    void saveShouldSaveSingleUser() {
        // pre-condition
        assertTrue(userPersistenceService.findByEmail(EMAIL_TEST_USER).isEmpty());

        // arrange
        var newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstname("firstname") //
                .withLastname("lastname") //
                .withEmail(EMAIL_TEST_USER) //
                .build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser);
        assertEquals(newUser, createdUser);

        // post-condition
        assertTrue(userPersistenceService.findByEmail(EMAIL_TEST_USER).isPresent());
    }

    @DisplayName("save() should save single user with user team list")
    @Test
    void saveShouldSaveSingleUserWithUserTeamList() {
        // pre-condition
        assertTrue(userPersistenceService.findByEmail(EMAIL_TEST_USER).isEmpty());

        // arrange
        var newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstname("firstname") //
                .withLastname("lastname") //
                .withEmail(EMAIL_TEST_USER) //
                .withUserTeamList(new ArrayList<>()).build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser);
        assertEquals(newUser, createdUser);

        // post-condition
        assertTrue(userPersistenceService.findByEmail(EMAIL_TEST_USER).isPresent());
    }

    @DisplayName("saveAll() should save list of users")
    @Test
    void saveAllShouldSaveListOfUsers() {
        // arrange
        var user1 = User.Builder.builder() //
                .withId(null) //
                .withFirstname("user1_firstname") //
                .withLastname("user1_lastname") //
                .withEmail("user1@test.ch") //
                .build();

        var user2 = User.Builder.builder() //
                .withId(null) //
                .withFirstname("user2_firstname") //
                .withLastname("user2_lastname") //
                .withEmail("user2@test.ch") //
                .build();

        // act
        List<User> createdUsers = iterableToList( //
                userPersistenceService.saveAll(List.of(user1, user2)));

        // assert
        assertEquals(2, createdUsers.size());
        assertEquals(user1, createdUsers.get(0));
        assertEquals(user2, createdUsers.get(1));
    }

    private List<User> iterableToList(Iterable<User> iterable) {
        List<User> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}