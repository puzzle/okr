package ch.puzzle.okr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.ES384;
import static org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS256;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .anyRequest()
                            .authenticated())
            .exceptionHandling(e -> e
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
}