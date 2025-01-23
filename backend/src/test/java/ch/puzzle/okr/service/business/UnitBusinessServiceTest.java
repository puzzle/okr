package ch.puzzle.okr.service.business;

import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.service.validation.UnitValidationService;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static ch.puzzle.okr.test.TestHelper.FTE_UNIT;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnitBusinessServiceTest {
    @Mock UnitPersistenceService unitPersistenceService;
    @Mock UnitValidationService unitValidationService;
    @InjectMocks UnitBusinessService unitBusinessService;


    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallGetAllMethodOnPersistenceService() {
        unitBusinessService.getAllUnits();
        verify(unitPersistenceService, times(1)).findAll();
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallGetAllByUserMethodOnPersistenceService() {
        Long id = 1L;
        unitBusinessService.findUnitsByUser(id);
        verify(unitPersistenceService, times(1)).findUnitsByUser(id);
        verify(unitValidationService, times(1)).validateOnGet(id);
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallFindUnitByNameMethodOnPersistenceService() {
        String name = "name";
        unitBusinessService.findUnitByName(name);
        verify(unitPersistenceService, times(1)).findUnitByUnitName(name);
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallGetIdMethodOnPersistenceService() {
        Long id = 1L;
        unitBusinessService.getEntityById(id);
        verify(unitPersistenceService, times(1)).findById(id);
        verify(unitValidationService, times(1)).validateOnGet(id);
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallCreateEntityMethodOnPersistenceService() {
        unitBusinessService.createEntity(FTE_UNIT);
        verify(unitPersistenceService, times(1)).save(FTE_UNIT);
        verify(unitValidationService, times(1)).validateOnCreate(FTE_UNIT);
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallUpdateMethodOnPersistenceService() {
        Long id = 1L;
        unitBusinessService.updateEntity(id, FTE_UNIT);
        verify(unitPersistenceService, times(1)).save(FTE_UNIT);
        verify(unitValidationService, times(1)).validateOnUpdate(id, FTE_UNIT);
    }

    @DisplayName("Should get alignment selection by quarter id and all teams except ignored team")
    @Test
    void shouldCallDeleteMethodOnPersistenceService() {
        Long id = 1L;
        unitBusinessService.deleteEntityById(id);
        verify(unitPersistenceService, times(1)).deleteById(id);
        verify(unitValidationService, times(1)).validateOnDelete(id);
    }

}
