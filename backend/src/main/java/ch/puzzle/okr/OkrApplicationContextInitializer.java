package ch.puzzle.okr;

import ch.puzzle.okr.multitenancy.HibernateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkrApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(OkrApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.info("Loading hibernate configuration from application properties");
        HibernateContext.extractAndSetHibernateConfig(applicationContext.getEnvironment());
    }

}