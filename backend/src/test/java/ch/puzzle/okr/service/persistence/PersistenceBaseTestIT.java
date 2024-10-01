package ch.puzzle.okr.service.persistence;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing the functionality of the abstract PersistenceBase and use UserRepository as example of a CrudRepository
 * implementation.
 */
@SpringIntegrationTest
public class PersistenceBaseTestIT {

    private User createdUser;

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

    @DisplayName("findById() should return single item if item with id exists")
    @Test
    void findByIdShouldReturnSingleItemIfItemWithIdExists() {
        User returnedUser = persistenceBase.findById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Paco", returnedUser.getFirstname());
        assertEquals("Eggimann", returnedUser.getLastname());
        assertEquals("peggimann@puzzle.ch", returnedUser.getEmail());
    }

    @DisplayName("findById() should throw exception if item with id does not exist")
    @Test
    void findByIdShouldThrowExceptionIfItemWithIdDoesNotExist() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("MODEL_WITH_ID_NOT_FOUND", exception.getReason());
    }

    @DisplayName("findById() should throw exception if id is null")
    @Test
    void findByIdShouldThrowExceptionIfIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
    }

    @DisplayName("findAll() should return all items as list")
    @Test
    void findAllShouldReturnAllItemsAsList() throws ResponseStatusException {
        List<User> userList = persistenceBase.findAll();

        assertEquals(7, userList.size());
    }

    @DisplayName("save() should add new item")
    @Test
    void saveShouldAddNewItem() throws ResponseStatusException {
        createdUser = persistenceBase.save(User.Builder.builder() //
                .withFirstname("Hans") //
                .withLastname("Muster") //
                .withEmail("hans.muster@puzzle.ch") //
                .build());

        assertNotNull(createdUser);
        assertEquals(persistenceBase.findAll().size(), 8);
    }

    @DisplayName("deleteById() should delete item")
    @Test
    void deleteByIdShouldDeleteItem() throws ResponseStatusException {
        assertNumberOfUsersInRepo(7);

        createdUser = persistenceBase.save(User.Builder.builder() //
                .withFirstname("Hans") //
                .withLastname("Muster") //
                .withEmail("hans.muster@puzzle.ch") //
                .build());

        assertNumberOfUsersInRepo(8);
        assertNotNull(persistenceBase.findById(createdUser.getId()));

        persistenceBase.deleteById(createdUser.getId());

        assertNumberOfUsersInRepo(7);
        assertUserNotFound(createdUser.getId());
    }

    private void assertNumberOfUsersInRepo(int n) {
        assertEquals(persistenceBase.findAll().size(), n);
    }

    private void assertUserNotFound(long userId) {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> persistenceBase.findById(userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("MODEL_WITH_ID_NOT_FOUND", exception.getReason());
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
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertEquals("DATA_HAS_BEEN_UPDATED", exception.getReason());
    }

}
