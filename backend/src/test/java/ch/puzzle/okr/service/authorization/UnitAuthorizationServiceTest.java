package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UnitBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.test.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ExtendWith(MockitoExtension.class)
class UnitAuthorizationServiceTest {

    private final AuthorizationUser adminUser = mockAuthorizationUser(glUser());
    private final Unit simpleUnit = Unit.Builder.builder().id(1L).unitName("Unit").createdBy(glUser()).build();
    private final Unit simpleUnit2 = Unit.Builder.builder().id(2L).unitName("Unit2").createdBy(bbtUser()).build();
    @Mock UnitBusinessService unitBusinessService;
    @Mock AuthorizationService authorizationService;
    @InjectMocks private UnitAuthorizationService unitAuthorizationService;

    @BeforeEach
    void setUp() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(adminUser);
    }

    @DisplayName("Should get all units")
    @Test
    void shouldCallFindAllBusinessMethod() {
        reset(authorizationService);

        unitAuthorizationService.getAllUnits();
        verify(unitBusinessService, times(1)).getAllUnits();
    }

    @DisplayName("Should get all units of User")
    @Test
    void shouldCallFindAllByUserBusinessMethod() {
        unitAuthorizationService.getUnitsOfUser();
        verify(unitBusinessService, times(1)).findUnitsByUser(adminUser.user().getId());
    }

    @DisplayName("Should call create Entity when creating a unit")
    @Test
    void shouldReturnCreatedUnit() {
        reset(authorizationService);

        unitAuthorizationService.createUnit(simpleUnit);
        verify(unitBusinessService, times(1)).createEntity(simpleUnit);
    }

    @DisplayName("Should validate owner when updating a unit and be successful")
    @Test
    void shouldValidateOwnerAndCallBusinessMethodWhenUpdateAndBeSuccessful() {
        Long id = 1L;
        when(unitBusinessService.getEntityById(id)).thenReturn(simpleUnit);
        unitAuthorizationService.updateUnit(id, simpleUnit);
        verify(unitBusinessService, times(1)).updateEntity(id, simpleUnit);
    }

    @DisplayName("Should validate owner when updating a unit and throw an exception")
    @Test
    void shouldValidateOwnerAndCallBusinessMethodWhenUpdateAndThrowError() {
        Long id = 1L;
        when(unitBusinessService.getEntityById(id)).thenReturn(simpleUnit2);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> unitAuthorizationService.updateUnit(id, simpleUnit));
        verify(unitBusinessService, times(0)).updateEntity(id, simpleUnit);
        assertEquals(FORBIDDEN, exception.getStatusCode());
    }

    @DisplayName("Should throw an exception when the user is authorized as a member")
    @Test
    void shouldValidateOwnerAndCallBusinessMethodWhenDeleteAndBeSuccessful() {
        Long id = 1L;
        when(unitBusinessService.getEntityById(id)).thenReturn(simpleUnit);
        unitAuthorizationService.deleteUnitById(id);
        verify(unitBusinessService, times(1)).deleteEntityById(id);
    }

    @DisplayName("Should throw an exception when the user is authorized as a member")
    @Test
    void shouldValidateOwnerAndCallBusinessMethodWhenDeleteAndThrowError() {
        Long id = 1L;
        when(unitBusinessService.getEntityById(id)).thenReturn(simpleUnit, simpleUnit2);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> unitAuthorizationService.deleteUnitById(id));
        verify(unitBusinessService, times(0)).deleteEntityById(id);
        assertEquals(FORBIDDEN, exception.getStatusCode());
    }
}
