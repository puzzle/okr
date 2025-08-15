package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.HikariContext.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.exception.HikariContextException;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;

public class HikariContextTest {
    @BeforeEach
    void setUp() {
        HikariContext.setHikariConfig(null);
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
        assertProperty(maximumPoolSize, hikariProperties);
    }

    @DisplayName("getHikariConfig() should throw exception if setHikariConfig() is not called before with valid configuration")
    @Test
    void getHikariConfigShouldThrowExceptionIfSetHikariConfigIsNotCalledBeforeWithValidConfiguration() {
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
        assertProperty(maximumPoolSize, hikariProperties);
    }

    private void assertProperty(String maximumPoolSize, Properties property) {
        assertEquals(maximumPoolSize, property.get(HIKARI_MAXIMUM_POOL_SIZE));
    }

}
