package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.HibernateContext.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.exception.HibernateContextException;
import java.util.Properties;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.env.ConfigurableEnvironment;

class HibernateContextTest {

    private static final String NOT_USED = "not_used";

    @BeforeEach
    void setUp() {
        resetHibernateConfig();
    }

    @DisplayName("setHibernateConfig() should throw exception if db config is null")
    @Test
    void setHibernateConfigShouldThrowExceptionIfDbConfigIsNull() {
        // arrange
        DbConfig dbConfig = null;

        // act + assert
        var exception = assertThrows(HibernateContextException.class, () -> setHibernateConfig(dbConfig));
        assertEquals("Invalid hibernate configuration null", exception.getMessage());
    }

    @ParameterizedTest(name = "setHibernateConfig() should throw exception if db config has null or empty values")
    @MethodSource("invalidDbConfig")
    void setHibernateConfigShouldThrowExceptionIfDbConfigHasNullOrEmptyValues(String url, String username,
                                                                              String password, String tenant) {

        // arrange
        var dbConfig = new DbConfig(url, username, password, tenant);

        // act + assert
        HibernateContextException exception = assertThrows(HibernateContextException.class,
                                                           () -> setHibernateConfig(dbConfig));
        assertTrue(exception.getMessage().startsWith("Invalid hibernate configuration"));
    }

    private static Stream<Arguments> invalidDbConfig() {
        return Stream
                .of( //
                    Arguments.of(null, "username", "password", "multiTenancy"), //
                    Arguments.of("", "username", "password", "multiTenancy"), //
                    Arguments.of("url", null, "password", "multiTenancy"), //
                    Arguments.of("url", "", "password", "multiTenancy"), //
                    Arguments.of("url", "username", null, "multiTenancy"), //
                    Arguments.of("url", "username", "", "multiTenancy"), //
                    Arguments.of("url", "username", "password", null), //
                    Arguments.of("url", "username", "password", ""));
    }

    @DisplayName("extractAndSetHibernateConfig() should extract hibernate properties from environment and cache it")
    @Test
    void extractAndSetHibernateConfigShouldExtractHibernatePropertiesFromEnvironmentAndCacheIt() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";

        var environment = mock(ConfigurableEnvironment.class);
        when(environment.getProperty(HIBERNATE_CONNECTION_URL)).thenReturn(url);
        when(environment.getProperty(HIBERNATE_CONNECTION_USERNAME)).thenReturn(username);
        when(environment.getProperty(HIBERNATE_CONNECTION_PASSWORD)).thenReturn(password);
        when(environment.getProperty(HIBERNATE_MULTITENANCY)).thenReturn(multiTenancy);

        // act
        extractAndSetHibernateConfig(environment);
        var hibernateProperties = getHibernateConfig();

        // assert
        assertNotNull(hibernateProperties);
        assertProperties(url, username, password, multiTenancy, hibernateProperties);
    }

    @DisplayName("getHibernateConfig() should throw exception if setHibernateConfig() is not called before with valid configuration")
    @Test
    void getHibernateConfigShouldThrowExceptionIfSetHibernateConfigIsNotCalledBeforeWithValidConfiguration() {
        // arrange: no DbConfig is set

        // act + assert
        var exception = assertThrows(HibernateContextException.class, HibernateContext::getHibernateConfig);
        assertEquals("No cached hibernate configuration found", exception.getMessage());
    }

    @DisplayName("getHibernateConfig() should return hibernate config as properties if db config is valid")
    @Test
    void getHibernateConfigShouldReturnHibernateConfigAsPropertiesIfDbConfigIsValid() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";
        var dbConfig = new DbConfig(url, username, password, multiTenancy);
        setHibernateConfig(dbConfig);

        // act
        var hibernateProperties = getHibernateConfig();

        // assert
        assertNotNull(hibernateProperties);
        assertProperties(url, username, password, multiTenancy, hibernateProperties);
    }

    @DisplayName("getHibernateConfigForTenantId() should throw exception if setHibernateConfig() is not called before with valid configuration")
    @Test
    void getHibernateConfigForTenantIdShouldThrowExceptionIfSetHibernateConfigIsNotCalledBeforeWithValidConfiguration() {
        // arrange: no DbConfig is set

        // act + assert
        var exception = assertThrows(RuntimeException.class, () -> getHibernateConfig("tenantId"));
        assertEquals("No cached hibernate configuration found (for tenant tenantId)", exception.getMessage());
    }

    @DisplayName("setHibernateConfigForTenantId() should throw exception when no tenant config is cached")
    @Test
    void setHibernateConfigForTenantIdShouldThrowExceptionWhenNoTenantConfigIsCached() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";
        var dbConfig = new DbConfig(url, username, password, multiTenancy);
        setHibernateConfig(dbConfig);

        var tenantId = "tenantId"; // but no tenant config is cached

        // act + assert
        var exception = assertThrows(RuntimeException.class, () -> getHibernateConfig(tenantId));
        assertEquals("No cached tenant configuration found (for tenant tenantId)", exception.getMessage());
    }

    @DisplayName("getHibernateConfigForTenantId() should return hibernate config with patched tenant username and password as properties if db config is valid")
    @Test
    void getHibernateConfigForTenantIdShouldReturnHibernateConfigWithPatchedTenantUserNameAndPasswordAsPropertiesIfDbConfigIsValid() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";
        var dbConfig = new DbConfig(url, username, password, multiTenancy);
        setHibernateConfig(dbConfig);

        String tenantId = "tenantId", tenantName = "tenantName", tenantPassword = "tenantPassword";
        var tenantConfig = new TenantConfigProvider.TenantConfig(tenantId,
                                                                 new String[]{},
                                                                 NOT_USED,
                                                                 NOT_USED,
                                                                 NOT_USED,
                                                                 null, // fly user config not used in test
                                                                 new TenantConfigProvider.DataSourceConfig(NOT_USED,
                                                                                                           NOT_USED,
                                                                                                           tenantName,
                                                                                                           tenantPassword,
                                                                                                           NOT_USED));

        TenantConfigs.add(tenantId, tenantConfig); // cache tenant db config

        // act
        var hibernateProperties = getHibernateConfig(tenantId);

        // assert
        assertNotNull(hibernateProperties);
        assertProperties(url, tenantName, tenantPassword, multiTenancy, hibernateProperties);
    }

    private void assertProperties(String url, String username, String password, String multiTenancy,
                                  Properties properties) {

        assertEquals(url, properties.get(HIBERNATE_CONNECTION_URL));
        assertEquals(username, properties.get(HIBERNATE_CONNECTION_USERNAME));
        assertEquals(password, properties.get(HIBERNATE_CONNECTION_PASSWORD));
        assertEquals(multiTenancy, properties.get(HIBERNATE_MULTITENANCY));
        assertEquals(url, properties.get(SPRING_DATASOURCE_URL));
        assertEquals(username, properties.get(SPRING_DATASOURCE_USERNAME));
        assertEquals(password, properties.get(SPRING_DATASOURCE_PASSWORD));
    }

}
