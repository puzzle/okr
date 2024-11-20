package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateHelperTest {
    private AuthorizationRegistrationService.UserUpdateHelper helper;

    @BeforeEach
    void setup() {
        helper = new AuthorizationRegistrationService.UserUpdateHelper();
    }

    @DisplayName("update userFromDB with Firstname and Lastname from token")
    @Test
    void updateUserFromWithTokenData() {
        // arrange
        User userFromDB = User.Builder.builder() //
                .withId(23L) //
                .withFirstname("firstname_from_db") //
                .withLastname("lastname_from_db") //
                .withEmail("a@b.ch") //
                .build();

        User userFromToken = User.Builder.builder() //
                .withFirstname("firstname_from_token") //
                .withLastname("lastname_from_token") //
                .build();

        // act
        User updatedUser = helper.setFirstLastNameFromToken(userFromDB, userFromToken);

        // assert
        assertEquals(23L, updatedUser.getId());
        assertEquals("a@b.ch", updatedUser.getEmail());
        assertEquals("firstname_from_token", updatedUser.getFirstname());
        assertEquals("lastname_from_token", updatedUser.getLastname());
    }

    @Test
    void updateUserAsNoChampion() {
        // arrange
        User noChampionUser = User.Builder.builder().withEmail("no@champions.ch").build();
        TenantConfigProvider.TenantConfig tenantConfig = new TenantConfigProvider.TenantConfig(null, //
                new String[] { "yes@champions.ch" }, null, null, null, null, null);

        // act
        User updatedUser = helper.setOkrChampionFromProperties(noChampionUser, tenantConfig);

        // assert
        assertFalse(updatedUser.isOkrChampion());
    }

    @Test
    void updateUserAsChampion() {
        // arrange
        User championUser = User.Builder.builder().withEmail("yes@champions.ch").build();
        TenantConfigProvider.TenantConfig tenantConfig = new TenantConfigProvider.TenantConfig(null, //
                new String[] { "yes@champions.ch" }, null, null, null, null, null);

        // act
        User updatedUser = helper.setOkrChampionFromProperties(championUser, tenantConfig);

        // assert
        assertTrue(updatedUser.isOkrChampion());
    }
}
