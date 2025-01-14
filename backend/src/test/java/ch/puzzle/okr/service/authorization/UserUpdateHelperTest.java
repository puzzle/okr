package ch.puzzle.okr.service.authorization;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserUpdateHelperTest {
    private AuthorizationRegistrationService.UserUpdateHelper helper;

    @BeforeEach
    void setup() {
        helper = new AuthorizationRegistrationService.UserUpdateHelper();
    }

    @DisplayName("Should update userFromDB with Firstname and Lastname from token")
    @Test
    void shouldUpdateUserFromDBWithTokenData() {
        // arrange
        User userFromDB = User.Builder
                .builder() //
                .withId(23L) //
                .withFirstName("firstname_from_db") //
                .withLastName("lastname_from_db") //
                .withEmail("a@b.ch") //
                .build();

        User userFromToken = User.Builder
                .builder() //
                .withFirstName("firstname_from_token") //
                .withLastName("lastname_from_token") //
                .build();

        // act
        User updatedUser = helper.setFirstLastNameFromToken(userFromDB, userFromToken);

        // assert
        assertEquals(23L, updatedUser.getId());
        assertEquals("a@b.ch", updatedUser.getEmail());
        assertEquals("firstname_from_token", updatedUser.getFirstName());
        assertEquals("lastname_from_token", updatedUser.getLastName());
    }

    @DisplayName("Should not update user as OKR champion when email is not listed")
    @Test
    void shouldNotUpdateUserAsOkrChampionWhenEmailIsNotListed() {
        // arrange
        User noChampionUser = User.Builder.builder().withEmail("no@champions.ch").build();
        TenantConfigProvider.TenantConfig tenantConfig = new TenantConfigProvider.TenantConfig(null, //
                                                                                               new String[]{
                                                                                                       "yes@champions.ch" },
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null, null);

        // act
        User updatedUser = helper.setOkrChampionFromProperties(noChampionUser, tenantConfig);

        // assert
        assertFalse(updatedUser.isOkrChampion());
    }

    @DisplayName("Should update user as OKR champion when email is listed")
    @Test
    void shouldUpdateUserAsOkrChampionWhenEmailIsListed() {
        // arrange
        User championUser = User.Builder.builder().withEmail("yes@champions.ch").build();
        TenantConfigProvider.TenantConfig tenantConfig = new TenantConfigProvider.TenantConfig(null, //
                                                                                               new String[]{
                                                                                                       "yes@champions.ch" },
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null, null);

        // act
        User updatedUser = helper.setOkrChampionFromProperties(championUser, tenantConfig);

        // assert
        assertTrue(updatedUser.isOkrChampion());
    }
}
