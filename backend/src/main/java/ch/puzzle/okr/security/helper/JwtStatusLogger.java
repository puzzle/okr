package ch.puzzle.okr.security.helper;

import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtStatusLogger {
    private static final Logger logger = LoggerFactory.getLogger(JwtStatusLogger.class);

    private JwtStatusLogger() {
    }

    public static void logStatus(String claim, Object context, String result) {
        logStatus(claim, context, result != null);
    }

    public static void logStatus(String claim, Object context, boolean isOk) {
        if (isOk) {
            logger
                    .info("Tenant: get claim '{}' from {}{}",
                          claim,
                          context.getClass().getSimpleName(),
                          statusToSymbol(isOk));
        } else {
            logger
                    .warn("Tenant: get claim '{}' from {}{}",
                          claim,
                          context.getClass().getSimpleName(),
                          statusToSymbol(isOk));
        }
    }

    public static void logStatus(String claim, Object context, ParseException e) {
        logger
                .warn("Tenant: get claim '{}' from {}{}",
                      claim,
                      context.getClass().getSimpleName(),
                      statusToSymbol(false),
                      e);
    }

    private static String statusToSymbol(boolean isOk) {
        return isOk ? " | OK" : " | FAILED";
    }
}
