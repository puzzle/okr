package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.test.TestHelper.getAllErrorKeys;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

/**
 * Testing the functionality of the abstract PersistenceBase and use UserRepository as example of a CrudRepository
 * implementation.
 * <p>
 * Tests depending on data from V100_0_0__TestData.sql
 */
@SpringIntegrationTest
public class PersistenceBaseTestIT {

    private User createdUser;

    private static final long NON_EXISTING_USER_ID = 321L;
    private static final long USER_PACO_ID = 1L;
    private static final User USER_WITHOUT_CONSTRAINTS = User.Builder.builder() //
            .withFirstname("Hans") //
            .withLastname("Muster") //
            .withEmail("hans.muster@puzzle.ch") //
            .build();

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

    @DisplayName("findById() should return single entity if entity with id exists")
    @Test
    void findByIdShouldReturnSingleEntityIfEntityWithIdExists() {
        var foundUser = persistenceBase.findById(USER_PACO_ID);

        assertEquals(USER_PACO_ID, foundUser.getId());
        assertUser("Paco", "Eggimann", "peggimann@puzzle.ch", foundUser);
    }

    @DisplayName("findById() should throw exception if entity with id does not exist")
    @Test
    void findByIdShouldThrowExceptionIfEntityWithIdDoesNotExist() {
        var exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(NON_EXISTING_USER_ID));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertErrorKey("MODEL_WITH_ID_NOT_FOUND", exception);
    }

    @DisplayName("findById() should throw exception if id is null")
    @Test
    void findByIdShouldThrowExceptionIfIdIsNull() {
        var exception = assertThrows(ResponseStatusException.class, () -> persistenceBase.findById(null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertErrorKey("ATTRIBUTE_NULL", exception);
    }

    @DisplayName("findAll() should return all entities as list")
    @Test
    void findAllShouldReturnAllEntitiesAsList() throws ResponseStatusException {
        var userList = persistenceBase.findAll();

        assertThat(userList.size()).isGreaterThanOrEqualTo(7);
    }

    @DisplayName("save() should add new entity")
    @Test
    void saveShouldAddNewEntity() throws ResponseStatusException {
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);

        assertNotNull(createdUser);
        assertUser("Hans", "Muster", "hans.muster@puzzle.ch", createdUser);
    }

    @DisplayName("save() should throw exception in the case of optimistic locking failure")
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

    @DisplayName("save() existing entity with different data should update existing entity")
    @Test
    void saveExistingEntityWithDifferentDataShouldUpdateExistingEntity() throws ResponseStatusException {
        // arrange
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);
        var createdUserId = createdUser.getId();
        var foundUser = persistenceBase.findById(createdUserId);

        // pro-condition
        assertEquals("Hans", createdUser.getFirstname());

        // act
        foundUser.setFirstname("Pekka");
        persistenceBase.save(foundUser);
        foundUser = persistenceBase.findById(createdUserId);

        // assert
        assertEquals(createdUserId, foundUser.getId());
        assertEquals("Pekka", foundUser.getFirstname());
    }

    @DisplayName("deleteById() should delete entity")
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
        assertEquals(expectedFirstName, currentUser.getFirstname());
        assertEquals(expectedLastName, currentUser.getLastname());
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
}