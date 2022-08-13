package sanotesapigateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import sanotesapigateway.config.KeycloakReactiveTokenInstrospector;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info =
@Info(title = "Gateway API", version = "1.0", description = "Documentation of Gateway API v1.0")
)
public class SanotesApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanotesApiGatewayApplication.class, args);
    }


    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ReactiveOpaqueTokenIntrospector keycloakIntrospector(OAuth2ResourceServerProperties props) {

        NimbusReactiveOpaqueTokenIntrospector delegate = new NimbusReactiveOpaqueTokenIntrospector(
                props.getOpaquetoken().getIntrospectionUri(),
                props.getOpaquetoken().getClientId(),
                props.getOpaquetoken().getClientSecret());

        return new KeycloakReactiveTokenInstrospector(delegate);
    }

    @Autowired
    RouteDefinitionLocator locator;

    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        assert definitions != null;
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId().replaceAll("-service", "");
                    groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());

                });
        groups.add(GroupedOpenApi.builder().pathsToMatch("/gateway/**").group("gateway").build());
        return groups;
    }

}
