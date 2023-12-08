package ch.puzzle.okr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    @Order(1) // Must be First order! Otherwise unauthorized Requests are sent to Controllers
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterAfter(new ForwardFilter(), BasicAuthenticationFilter.class);
        setHeaders(http);
        logger.debug("*** apiSecurityFilterChain reached");
        return http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(e -> e.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())).build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityHeadersFilter(HttpSecurity http) throws Exception {
        logger.debug("*** SecurityHeader reached");
        return setHeaders(http).build();
    }

    private HttpSecurity setHeaders(HttpSecurity http) throws Exception {
        http.headers(h -> h.contentSecurityPolicy(e -> e.policyDirectives("default-src 'self';"
                + "script-src 'self' 'unsafe-inline';" + "        style-src 'self' 'unsafe-inline';"
                + "        object-src 'none';" + "        base-uri 'self';"
                + "        connect-src 'self' https://sso.puzzle.ch http://localhost:8544; https://sso.puzzle.ch/auth/realms/pitc/.well-known/openid-configuration; https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch;"
                + "        font-src 'self';" + "        frame-src 'self';" + "        img-src 'self';"
                + "        manifest-src 'self';" + "        media-src 'self';" + "        worker-src 'none';"))
                .crossOriginEmbedderPolicy(coepCustomizer -> coepCustomizer
                        .policy(CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP))
                .crossOriginOpenerPolicy(coopCustomizer -> coopCustomizer
                        .policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN))
                .crossOriginResourcePolicy(corpCustomizer -> corpCustomizer
                        .policy(CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN))
                .addHeaderWriter(new StaticHeadersWriter("X-Permitted-Cross-Domain-Policies", "none")));
        return http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .xssProtection(e -> e.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .httpStrictTransportSecurity(e -> e.includeSubDomains(true).maxAgeInSeconds(31536000))
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                .permissionsPolicy(
                        permissions -> permissions.policy("accelerometer=(), ambient-light-sensor=(), autoplay=(), "
                                + "battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), "
                                + "execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(),"
                                + " geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), "
                                + "midi=(), navigation-override=(), payment=(), picture-in-picture=(),"
                                + " publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(self), "
                                + "usb=(), web-share=(), xr-spatial-tracking=()")));
    }
}