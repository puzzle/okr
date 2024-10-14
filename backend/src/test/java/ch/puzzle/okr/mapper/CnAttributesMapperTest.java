package ch.puzzle.okr.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

public class CnAttributesMapperTest {

    private final CnAttributesMapper cnAttributesMapper = new CnAttributesMapper();

    @DisplayName("for Cn AttributeId should return AttributeValue")
    @Test
    void forCnAttributeIdShouldReturnAttributeValue() throws NamingException {
        Attributes attributes = new BasicAttributes();
        attributes.put("cn", "Mango");

        Assertions.assertEquals("Mango", cnAttributesMapper.mapFromAttributes(attributes));
    }

    @DisplayName("for non Cn AttributeId should return null")
    @Test
    void forNonCnAttributeIdShouldReturnNull() throws NamingException {
        Attributes attributes = new BasicAttributes();
        attributes.put("ou", "Juicy, Fruit");

        Assertions.assertNull(cnAttributesMapper.mapFromAttributes(attributes));
    }
}
