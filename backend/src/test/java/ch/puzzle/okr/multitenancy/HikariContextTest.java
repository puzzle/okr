package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.HikariContext.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.exception.HikariContextException;
import java.util.Properties;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.env.ConfigurableEnvironment;

public class HikariContextTest {
    @BeforeEach
    void setUp() {
        resetHikariConfig();
    }

    @DisplayName("setHikariConfig() should throw exception if db config is null")
    @Test
    void setHikariConfigShouldThrowExceptionIfDbConfigIsNull() {
        // arrange
        HikariContext.DbConfig dbConfig = null;

        // act + assert
        HikariContextException exception = assertThrows(HikariContextException.class, () -> setHikariConfig(dbConfig));
        assertEquals("Invalid hikari configuration null", exception.getMessage());
    }

    private static Stream<Arguments> invalidDbConfig() {
        return Stream
                .of( //
                    Arguments.of(null, ""), //
                    Arguments.of("")); //
    }

    @ParameterizedTest(name = "setHikariConfig() should throw exception if db config has null or empty values")
    @MethodSource("invalidDbConfig")
    void setHikariConfigShouldThrowExceptionIfDbConfigHasNullOrEmptyValues(String maximumPoolSize) {

        // arrange
        DbConfig dbConfig = new DbConfig(maximumPoolSize);

        // act + assert
        HikariContextException exception = assertThrows(HikariContextException.class, () -> setHikariConfig(dbConfig));
        assertTrue(exception.getMessage().startsWith("Invalid hikari configuration"));
    }

    @DisplayName("extractAndSetHikariConfig() should extract hikari properties from environment and set it")
    @Test
    void extractAndSetHikariConfigShouldExtractHikariPropertiesFromEnvironmentAndSetIt() {
        // arrange
        String maximumPoolSize = "3";

        ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
        when(environment.getProperty(HIKARI_MAXIMUM_POOL_SIZE)).thenReturn(maximumPoolSize);

        // act
        extractAndSetHikariConfig(environment);
        Properties hikariProperties = getHikariConfig();

        // assert
        assertNotNull(hikariProperties);
        assertProperties(maximumPoolSize, hikariProperties);
    }

    @DisplayName("getHikariConfig() should throw exception if setHikariConfig() is not called before with valid configuration")
    @Test
    void getHikariConfigShouldThrowExceptionIfSetHikariConfigIsNotCalledBeforeWithValidConfiguration() {
        // arrange

        // act + assert
        HikariContextException exception = assertThrows(HikariContextException.class, HikariContext::getHikariConfig);
        assertEquals("No cached hikari configuration found", exception.getMessage());
    }

    @DisplayName("getHikariConfig() should return hikari config as properties if db config is valid")
    @Test
    void getHikariConfigShouldReturnHikariConfigAsPropertiesIfDbConfigIsValid() {
        // arrange
        String maximumPoolSize = "3";
        DbConfig dbConfig = new DbConfig(maximumPoolSize);
        setHikariConfig(dbConfig);

        // act
        Properties hikariProperties = getHikariConfig();

        // assert
        assertNotNull(hikariProperties);
        assertProperties(maximumPoolSize, hikariProperties);
    }

    private void assertProperties(String maximumPoolSize, Properties properties) {

        assertEquals(maximumPoolSize, properties.get(HIKARI_MAXIMUM_POOL_SIZE));
    }

}
