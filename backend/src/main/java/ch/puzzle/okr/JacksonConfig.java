package ch.puzzle.okr;

import ch.puzzle.okr.deserializer.CheckInDeserializer;
import ch.puzzle.okr.deserializer.KeyResultDeserializer;
import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule typeModule(CheckInDeserializer checkInDeserializer,
                                   KeyResultDeserializer keyResultDeserializer) {
        SimpleModule module = new SimpleModule("TypeModule");

        module.addDeserializer(CheckInDto.class, checkInDeserializer);
        module.addDeserializer(KeyResultDto.class, keyResultDeserializer);

        return module;
    }
}
