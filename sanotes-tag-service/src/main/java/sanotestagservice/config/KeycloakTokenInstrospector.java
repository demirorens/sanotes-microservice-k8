package sanotestagservice.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom ReactiveTokenIntrospector to map realm roles into Spring GrantedAuthorities
 */
public class KeycloakTokenInstrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;

    public KeycloakTokenInstrospector(OpaqueTokenIntrospector delegate) {
        this.delegate = delegate;
    }


    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
                delegate.introspect(token).getName(),
                delegate.introspect(token).getAttributes(),
                extractAuthorities(delegate.introspect(token)));
        return principal;
    }

    protected OAuth2AuthenticatedPrincipal mapPrincipal(OAuth2AuthenticatedPrincipal principal) {

        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(),
                principal.getAttributes(),
                extractAuthorities(principal));
    }

    protected Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {

        //
        Map<String, List<String>> realm_access = principal.getAttribute("realm_access");
        List<String> roles = realm_access.getOrDefault("roles", Collections.emptyList());
        List<GrantedAuthority> rolesAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Set<GrantedAuthority> allAuthorities = new HashSet<>();
        allAuthorities.addAll(principal.getAuthorities());
        allAuthorities.addAll(rolesAuthorities);

        return allAuthorities;
    }

}