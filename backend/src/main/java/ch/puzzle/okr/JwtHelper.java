package ch.puzzle.okr;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class JwtHelper {

    private final TenantConfigProvider tenantConfigProvider;

    public JwtHelper(TenantConfigProvider tenantConfigProvider) {
        this.tenantConfigProvider = tenantConfigProvider;
    }

    public String getTenantFromIssuer(String issuer) {
        final String tenantId = issuer.split("/realms/")[1];

        // Ensure we return only tenantIds for realms which really exist
        return this.tenantConfigProvider.getTenantConfigById(tenantId).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format("Cannot find tenant for issuer{0}", issuer)))
                .tenantId();
    }
}