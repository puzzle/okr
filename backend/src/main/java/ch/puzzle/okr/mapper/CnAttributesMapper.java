package ch.puzzle.okr.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

@Component
public class CnAttributesMapper implements AttributesMapper<String> {

    @Override
    public String mapFromAttributes(Attributes attributes) throws NamingException {
        Attribute cnAttribute = attributes.get("cn");
        if (cnAttribute != null) {
            return cnAttribute.get().toString();
        }
        return null;
    }
}
