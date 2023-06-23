package ch.puzzle.okr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
                .permissionsPolicy(
                        permissions -> permissions.policy("accelerometer=(), ambient-light-sensor=(), autoplay=(), "
                                + "battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), "
                                + "execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(),"
                                + " geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), "
                                + "midi=(), navigation-override=(), payment=(), picture-in-picture=(),"
                                + " publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(self), "
                                + "usb=(), web-share=(), xr-spatial-tracking=()"))
                .and().xssProtection().block(false).and().httpStrictTransportSecurity().includeSubDomains(true)
                .maxAgeInSeconds(31536000).and().defaultsDisabled().contentTypeOptions());

        http.antMatcher("/api/**")
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
}