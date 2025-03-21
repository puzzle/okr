package ch.puzzle.okr;

import static org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER;
import static org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private static final CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy OPENER_SAME_ORIGIN = CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN;
    private static final CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy RESOURCE_SAME_ORIGIN = CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN;

    private String connectSrc;

    @Bean
    @Order(1) // Must be First order! Otherwise unauthorized Requests are sent to Controllers
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, @Value("${connect.src}") String connectSrc)
            throws Exception {

        this.connectSrc = connectSrc;
        setHeaders(http);
        http.addFilterAfter(new ForwardFilter(), BasicAuthenticationFilter.class);
        logger.debug("*** apiSecurityFilterChain reached");
        setHeaders(http);
        return http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(e -> e.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    JWTProcessor<SecurityContext> jwtProcessor(JWTClaimsSetAwareJWSKeySelector<SecurityContext> keySelector) {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWTClaimsSetAwareJWSKeySelector(keySelector);
        return jwtProcessor;
    }

    @Bean
    JwtDecoder jwtDecoder(JWTProcessor<SecurityContext> jwtProcessor, OAuth2TokenValidator<Jwt> jwtValidator) {
        NimbusJwtDecoder decoder = new NimbusJwtDecoder(jwtProcessor);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(),
                                                                                   jwtValidator);
        decoder.setJwtValidator(validator);
        return decoder;
    }

    private HttpSecurity setHeaders(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers
                        .contentSecurityPolicy(c -> c.policyDirectives(okrContentSecurityPolicy()))
                        .crossOriginEmbedderPolicy(c -> c.policy(REQUIRE_CORP))
                        .crossOriginOpenerPolicy(c -> c.policy(OPENER_SAME_ORIGIN))
                        .crossOriginResourcePolicy(c -> c.policy(RESOURCE_SAME_ORIGIN))
                        .addHeaderWriter(new StaticHeadersWriter("X-Permitted-Cross-Domain-Policies", "none"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .xssProtection(c -> c.headerValue(ENABLED_MODE_BLOCK))
                        .httpStrictTransportSecurity(c -> c.includeSubDomains(true).maxAgeInSeconds(31536000))
                        .referrerPolicy(c -> c.policy(NO_REFERRER))
                        .permissionsPolicyHeader(c -> c.policy(okrPermissionPolicy())));
    }

    private String okrContentSecurityPolicy() {
        return "default-src 'self';" //
               + "script-src 'self' 'unsafe-inline';" //
               + "        style-src 'self' 'unsafe-inline';" //
               + "        object-src 'none';" //
               + "        base-uri 'self';" //
               + "        connect-src 'self' " + connectSrc + ";" //
               + "        font-src 'self';" //
               + "        frame-src 'self';" //
               + "        img-src 'self' data: ;" //
               + "        manifest-src 'self';" //
               + "        media-src 'self';" //
               + "        worker-src 'none';"; //
    }

    private String okrPermissionPolicy() {
        return "accelerometer=(), ambient-light-sensor=(), autoplay=(), "
               + "battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), "
               + "execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(),"
               + " geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), "
               + "midi=(), navigation-override=(), payment=(), picture-in-picture=(),"
               + " publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(self), "
               + "usb=(), web-share=(), xr-spatial-tracking=()";
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}