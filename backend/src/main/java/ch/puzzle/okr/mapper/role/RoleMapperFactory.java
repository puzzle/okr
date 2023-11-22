package ch.puzzle.okr.mapper.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperFactory {
    @Autowired
    private ApplicationContext appContext;
    private RoleMapper roleMapper;

    public synchronized RoleMapper getRoleMapper() {
        if (roleMapper == null) {
            // place to load configured role mapper instead of default role mapper
            roleMapper = appContext.getBean(DefaultRoleMapper.class);
        }
        return roleMapper;
    }
}
