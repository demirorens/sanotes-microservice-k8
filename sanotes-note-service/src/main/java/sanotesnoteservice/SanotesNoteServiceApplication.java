package sanotesnoteservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import sanotesnoteservice.config.KeycloakTokenInstrospector;
import sanotesnoteservice.security.JwtAuthenticationFilter;
import sanotesnoteservice.security.RequestJwtInterceptor;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableMongoRepositories
@EnableJpaRepositories
@EnableFeignClients
@SpringBootApplication
@SecurityScheme(name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info =
    @Info(title = "Note API", version = "1.0", description = "Documentation of Note API v1.0")
)
public class SanotesNoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanotesNoteServiceApplication.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new TomcatServletWebServerFactory() {
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
                    ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxHeaderCount(300);
                }
            }
        };
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public RequestJwtInterceptor requestJwtInterceptor() {
        return new RequestJwtInterceptor();
    }

    @Bean
    public OpaqueTokenIntrospector keycloakIntrospector(OAuth2ResourceServerProperties props) {

        NimbusOpaqueTokenIntrospector delegate = new NimbusOpaqueTokenIntrospector(
                props.getOpaquetoken().getIntrospectionUri(),
                props.getOpaquetoken().getClientId(),
                props.getOpaquetoken().getClientSecret());

        return new KeycloakTokenInstrospector(delegate);
    }

}
