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

    @BeforeEach
    void setUp() {
        HibernateContext.setHibernateConfig(null);
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

    @ParameterizedTest(name = "extractAndSetHibernateConfig() should throw an exception if dbConfig has null or empty values")
    @MethodSource("invalidDbConfig")
    void extractAndSetHibernateConfigShouldThrowExceptionIfDbConfigHasNullOrEmptyValues(String url, String username, String password, String multiTenancy) {
        // arrange
        ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
        when(environment.getProperty(HIBERNATE_CONNECTION_URL)).thenReturn(url);
        when(environment.getProperty(HIBERNATE_CONNECTION_USERNAME)).thenReturn(username);
        when(environment.getProperty(HIBERNATE_CONNECTION_PASSWORD)).thenReturn(password);
        when(environment.getProperty(HIBERNATE_MULTITENANCY)).thenReturn(multiTenancy);

        // act + assert
        HibernateContextException exception = assertThrows(HibernateContextException.class,
                () -> extractAndSetHibernateConfig(environment));
        assertTrue(exception.getMessage().startsWith("Invalid hibernate configuration"));
    }

    @DisplayName("extractAndSetHibernateConfig() should extract hibernate properties from environment and set it")
    @Test
    void extractAndSetHibernateConfigShouldExtractHibernatePropertiesFromEnvironmentAndSetIt() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";

        ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
        when(environment.getProperty(HIBERNATE_CONNECTION_URL)).thenReturn(url);
        when(environment.getProperty(HIBERNATE_CONNECTION_USERNAME)).thenReturn(username);
        when(environment.getProperty(HIBERNATE_CONNECTION_PASSWORD)).thenReturn(password);
        when(environment.getProperty(HIBERNATE_MULTITENANCY)).thenReturn(multiTenancy);

        // act
        extractAndSetHibernateConfig(environment);
        Properties hibernateProperties = getHibernateConfig();

        // assert
        assertNotNull(hibernateProperties);
        assertProperties(url, username, password, multiTenancy, hibernateProperties);
    }

    @DisplayName("getHibernateConfig() should throw exception if setHibernateConfig() is not called before with valid configuration")
    @Test
    void getHibernateConfigShouldThrowExceptionIfSetHibernateConfigIsNotCalledBeforeWithValidConfiguration() {
        // act + assert
        HibernateContextException exception = assertThrows(HibernateContextException.class,
                                                           HibernateContext::getHibernateConfig);
        assertEquals("No cached hibernate configuration found", exception.getMessage());
    }

    @DisplayName("getHibernateConfig() should return hibernate config as properties if db config is valid")
    @Test
    void getHibernateConfigShouldReturnHibernateConfigAsPropertiesIfDbConfigIsValid() {
        // arrange
        String url = "url", username = "username", password = "password", multiTenancy = "multiTenancy";
        DbConfig dbConfig = new DbConfig(url, username, password, multiTenancy);
        setHibernateConfig(dbConfig);

        // act
        Properties hibernateProperties = getHibernateConfig();

        // assert
        assertNotNull(hibernateProperties);
        assertProperties(url, username, password, multiTenancy, hibernateProperties);
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
