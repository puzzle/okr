package ch.puzzle.okr.mapper.role;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringIntegrationTest
public class RoleMapperFactoryIT {

    @Autowired
    private RoleMapperFactory roleMapperFactory;

    @Test
    void getRoleMapper() {
        RoleMapper mapper = roleMapperFactory.getRoleMapper();
        assertNotNull(mapper);
        assertSame(mapper, roleMapperFactory.getRoleMapper());
    }
}
