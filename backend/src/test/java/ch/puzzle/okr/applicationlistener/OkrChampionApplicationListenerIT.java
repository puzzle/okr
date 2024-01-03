package ch.puzzle.okr.applicationlistener;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringIntegrationTest
class OkrChampionApplicationListenerIT {

    @Autowired
    private UserPersistenceService userPersistenceService;

    @Autowired
    private OkrChampionApplicationListener okrChampionApplicationListener;

    @Test
    public void listenerShouldSetOkrChampionsCorrectly() {
        var userRichard = User.Builder.builder().withFirstname("Richard").withLastname("Eberhard")
                .withEmail("richard.eberhard@puzzle.ch").build();
        var userMaria = User.Builder.builder().withFirstname("Maria").withLastname("Gerber")
                .withEmail("maria.gerber@puzzle.ch").build();
        var userAndrea = User.Builder.builder().withFirstname("Andrea").withLastname("Nydegger")
                .withEmail("andrea.nydegger@puzzle.ch").build();

        userPersistenceService.getOrCreateUser(userRichard);
        userPersistenceService.getOrCreateUser(userMaria);
        userPersistenceService.getOrCreateUser(userAndrea);

        setField(okrChampionApplicationListener, "okrChampionEmails",
                "maria.gerber@puzzle.ch, richard.eberhard@puzzle.ch");

        okrChampionApplicationListener.onApplicationEvent(null);

        var userRichardSaved = userPersistenceService.findByEmail(userRichard.getEmail());
        var userMariaSaved = userPersistenceService.findByEmail(userMaria.getEmail());
        var userAndreaSaved = userPersistenceService.findByEmail(userAndrea.getEmail());

        assertTrue(userRichardSaved.get().isOkrChampion());
        assertTrue(userMariaSaved.get().isOkrChampion());
        assertFalse(userAndreaSaved.get().isOkrChampion());

    }

}