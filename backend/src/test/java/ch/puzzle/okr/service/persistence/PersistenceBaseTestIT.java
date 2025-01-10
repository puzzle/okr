package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.test.TestHelper.getAllErrorKeys;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Testing the functionality of the abstract PersistenceBase and use
 * UserRepository as example of a CrudRepository implementation.
 * <p>
 * Tests depending on data from V100_0_0__TestData.sql
 */
@SpringIntegrationTest
class PersistenceBaseTestIT {

    private User createdUser;

    private static final long NON_EXISTING_USER_ID = 321L;
    private static final long USER_PACO_ID = 1L;
    private static final User USER_WITHOUT_CONSTRAINTS = createUserWithUniqueName("Hans");

    @Autowired
    private PersistenceBase<User, Long, UserRepository> persistenceBase;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            persistenceBase.deleteById(createdUser.getId());
            createdUser = null;
        }
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should return single entity on findById() when entity with id exists")
    @Test
    void findByIdShouldReturnSingleEntityIfEntityWithIdExists() {
        var foundUser = persistenceBase.findById(USER_PACO_ID);

        assertEquals(USER_PACO_ID, foundUser.getId());
        assertUser("Paco", "Eggimann", "peggimann@puzzle.ch", foundUser);
    }

    @DisplayName("Should throw exception on findById() when entity with id does not exist")
    @Test
    void findByIdShouldThrowExceptionIfEntityWithIdDoesNotExist() {
        var exception = assertThrows(ResponseStatusException.class,
                                     () -> persistenceBase.findById(NON_EXISTING_USER_ID));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertErrorKey("MODEL_WITH_ID_NOT_FOUND", exception);
    }

    @DisplayName("Should throw exception on findById() when id is null")
    @Test
    void findByIdShouldThrowExceptionIfIdIsNull() {
        var exception = assertThrows(ResponseStatusException.class, () -> persistenceBase.findById(null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertErrorKey("ATTRIBUTE_NULL", exception);
    }

    @DisplayName("Should return all entities as list on findAll()")
    @Test
    void findAllShouldReturnAllEntitiesAsList() throws ResponseStatusException {
        var userList = persistenceBase.findAll();

        assertThat(userList).hasSizeGreaterThanOrEqualTo(7);

    }

    @DisplayName("Should add new entity on save()")
    @Test
    void saveShouldAddNewEntity() throws ResponseStatusException {
        User uniqueUser = createUserWithUniqueName("Fritz");
        createdUser = persistenceBase.save(uniqueUser);

        assertNotNull(createdUser);
        assertUser("Fritz", "Muster", "hans.muster@puzzle.ch", createdUser);
    }

    @DisplayName("Should throw exception on save() in the case of optimistic locking failure")
    @Test
    void saveShouldThrowExceptionInTheCaseOfOptimisticLockingFailure() throws ResponseStatusException {
        // arrange
        var testRepository = mock(UserRepository.class);
        when(testRepository.save(any())).thenThrow(OptimisticLockingFailureException.class);

        var persistenceBaseForTest = new PersistenceBase<>(testRepository) {
            @Override
            public String getModelName() {
                return "for_test";
            }
        };

        // act + assert
        var exception = assertThrows(ResponseStatusException.class, () -> persistenceBaseForTest.save(createdUser));

        // assert
        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertErrorKey("DATA_HAS_BEEN_UPDATED", exception);
    }

    @DisplayName("Should updated existing entity with new data on save()")
    @Test
    void saveExistingEntityWithDifferentDataShouldUpdateExistingEntity() throws ResponseStatusException {
        // arrange
        User uniqueUser = createUserWithUniqueName("Ueli");
        createdUser = persistenceBase.save(uniqueUser);
        var createdUserId = createdUser.getId();
        var foundUser = persistenceBase.findById(createdUserId);

        // pro-condition
        assertEquals("Ueli", createdUser.getFirstName());

        // act
        foundUser.setFirstName("Pekka");
        persistenceBase.save(foundUser);
        foundUser = persistenceBase.findById(createdUserId);

        // assert
        assertEquals(createdUserId, foundUser.getId());
        assertEquals("Pekka", foundUser.getFirstName());
    }

    @DisplayName("Should delete entity on deleteById()")
    @Test
    void deleteByIdShouldDeleteEntity() throws ResponseStatusException {
        // arrange
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);
        var createdUserId = createdUser.getId();
        assertNotNull(persistenceBase.findById(createdUserId));

        // act
        persistenceBase.deleteById(createdUserId);

        // assert
        assertEntityNotFound(createdUserId);
    }

    private static void assertUser(String expectedFirstName, String expectedLastName, String expectedEmail,
                                   User currentUser) {
        assertEquals(expectedFirstName, currentUser.getFirstName());
        assertEquals(expectedLastName, currentUser.getLastName());
        assertEquals(expectedEmail, currentUser.getEmail());
    }

    private void assertErrorKey(String errorKey, ResponseStatusException exception) {
        var errorKeys = getAllErrorKeys(List.of(new ErrorDto(errorKey, List.of("User"))));
        assertTrue(errorKeys.contains(exception.getReason()));
    }

    private void assertEntityNotFound(long entityId) {
        var exception = assertThrows(ResponseStatusException.class, () -> persistenceBase.findById(entityId));
        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertErrorKey("MODEL_WITH_ID_NOT_FOUND", exception);
    }

    private static User createUserWithUniqueName(String name) {
        return User.Builder
                .builder()
                .withFirstName(name)
                .withLastName("Muster")
                .withEmail("hans.muster@puzzle.ch")
                .build();
    }
}