package sanotesapigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private ReactiveOpaqueTokenIntrospector introspector;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET,
                        "/v3/api-docs/**",
                        "/webjars/swagger-ui/**",
                        "/swagger-ui.html",
                        "/openapi/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/user/auth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/user/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/tag/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/note/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/notebook/v3/api-docs/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaqueToken -> opaqueToken
                                .introspector(introspector)
                        )
                );
        // .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::opaqueToken);
        return http.build();
    }




}
