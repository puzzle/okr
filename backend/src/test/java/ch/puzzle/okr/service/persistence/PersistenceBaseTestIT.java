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
 */
@SpringIntegrationTest
public class PersistenceBaseTestIT {

    private User createdUser;

    private static final long ID_USER_PACO = 1L;
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

    @Test
    void findByIdShouldReturnSingleEntityIfEntityWithIdExists() {
        User foundUser = persistenceBase.findById(ID_USER_PACO);

        assertEquals(ID_USER_PACO, foundUser.getId());
        assertUser("Paco", "Eggimann", "peggimann@puzzle.ch", foundUser);
    }

    @Test
    void findByIdShouldThrowExceptionIfEntityWithIdDoesNotExist() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(321L));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertErrorKey("MODEL_WITH_ID_NOT_FOUND", exception);
    }

    @Test
    void findByIdShouldThrowExceptionIfIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertErrorKey("ATTRIBUTE_NULL", exception);
    }

    @Test
    void findAllShouldReturnAllEntitiesAsList() throws ResponseStatusException {
        List<User> userList = persistenceBase.findAll();

        assertThat(userList.size()).isGreaterThanOrEqualTo(7);
    }

    @Test
    void saveShouldAddNewEntity() throws ResponseStatusException {
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);

        assertNotNull(createdUser);
        assertUser("Hans", "Muster", "hans.muster@puzzle.ch", createdUser);
    }

    @Test
    void saveExistingEntityWithDifferentDataShouldUpdateExistingEntity() throws ResponseStatusException {
        // arrange
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);
        Long createdUserId = createdUser.getId();
        User foundUser = persistenceBase.findById(createdUserId);

        // act
        foundUser.setFirstname("Pekka");
        persistenceBase.save(foundUser);
        foundUser = persistenceBase.findById(createdUserId);

        // assert
        assertEquals(createdUserId, foundUser.getId());
        assertEquals("Pekka", foundUser.getFirstname());
    }

    @Test
    void deleteByIdShouldDeleteEntity() throws ResponseStatusException {
        // arrange
        createdUser = persistenceBase.save(USER_WITHOUT_CONSTRAINTS);
        Long createdUserId = createdUser.getId();
        assertNotNull(persistenceBase.findById(createdUserId));

        // act
        persistenceBase.deleteById(createdUserId);

        // assert
        assertEntityNotFound(createdUserId);
    }

    @DisplayName("deleteById() should throw exception in the case of optimistic locking failure")
    @Test
    void deleteByIdShouldThrowExceptionInTheCaseOfOptimisticLockingFailure() throws ResponseStatusException {
        // arrange
        UserRepository testRepository = mock(UserRepository.class);
        when(testRepository.save(any())).thenThrow(OptimisticLockingFailureException.class);

        PersistenceBase<User, Long, UserRepository> persistenceBaseForTest = new PersistenceBase<>(testRepository) {
            @Override
            public String getModelName() {
                return "for_test";
            }
        };

        // act + assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBaseForTest.save(createdUser));

        // assert
        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertErrorKey("DATA_HAS_BEEN_UPDATED", exception);
    }

    private static void assertUser(String expectedFirstName, String expectedLastName, String expectedEmail,
            User currentUser) {
        assertEquals(expectedFirstName, currentUser.getFirstname());
        assertEquals(expectedLastName, currentUser.getLastname());
        assertEquals(expectedEmail, currentUser.getEmail());
    }

    private void assertErrorKey(String errorKey, ResponseStatusException exception) {
        List<String> errorKeys = getAllErrorKeys(List.of(new ErrorDto(errorKey, List.of("User"))));
        assertTrue(errorKeys.contains(exception.getReason()));
    }

    private void assertEntityNotFound(long entityId) {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(entityId));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertErrorKey("MODEL_WITH_ID_NOT_FOUND", exception);
    }
}
