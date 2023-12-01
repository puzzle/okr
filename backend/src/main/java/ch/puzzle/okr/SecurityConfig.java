package ch.puzzle.okr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.header.writers.*;

@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    @Order(1) // Must be First order! Otherwise unauthorized Requests are sent to Controllers
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        setHeaders(http);
        logger.debug("*** apiSecurityFilterChain reached");
        return http.antMatcher("/api/**")
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt).build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityHeadersFilter(HttpSecurity http) throws Exception {
        logger.debug("*** SecurityHeader reached");
        return setHeaders(http).build();
    }

    private HttpSecurity setHeaders(HttpSecurity http) throws Exception {
        http.headers().contentSecurityPolicy("default-src 'self';" + "script-src 'self' 'unsafe-inline';"
                + "        style-src 'self' 'unsafe-inline';" + "        object-src 'none';"
                + "        base-uri 'self';"
                + "        connect-src 'self' https://sso.puzzle.ch http://localhost:8544; https://sso.puzzle.ch/auth/realms/pitc/.well-known/openid-configuration; https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch;"
                + "        font-src 'self';" + "        frame-src 'self';" + "        img-src 'self';"
                + "        manifest-src 'self';" + "        media-src 'self';" + "        worker-src 'none';").and()
                .crossOriginEmbedderPolicy(coepCustomizer -> coepCustomizer
                        .policy(CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP).and()
                        .crossOriginOpenerPolicy(coopCustomizer -> coopCustomizer
                                .policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN).and()
                                .crossOriginResourcePolicy(corpCustomizer -> corpCustomizer.policy(
                                        CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN)
                                        .and().addHeaderWriter(new StaticHeadersWriter(
                                                "X-Permitted-Cross-Domain-Policies", "none")))));
        return http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                .permissionsPolicy(
                        permissions -> permissions.policy("accelerometer=(), ambient-light-sensor=(), autoplay=(), "
                                + "battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), "
                                + "execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(),"
                                + " geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), "
                                + "midi=(), navigation-override=(), payment=(), picture-in-picture=(),"
                                + " publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(self), "
                                + "usb=(), web-share=(), xr-spatial-tracking=()"))
                .and().xssProtection().block(false).and().httpStrictTransportSecurity().includeSubDomains(true)
                .maxAgeInSeconds(31536000));
    }
}