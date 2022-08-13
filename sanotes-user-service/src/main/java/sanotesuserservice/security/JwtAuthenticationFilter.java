package sanotesuserservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private OpaqueTokenIntrospector introspector;

    // Filter to use incase of need
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            String token = getJwtFromRequest(request);
            if (token != null && !token.isEmpty()) {
//                Enumeration<String> stringEnumeration = request.getHeaderNames();
//                while (stringEnumeration.hasMoreElements()) {
//                    String s = stringEnumeration.nextElement();
//                    System.out.println(s + " : " + request.getHeader(s));
//                }
//                OAuth2AuthenticatedPrincipal principal = introspector.introspect(token);
////                System.out.println("TOKEN: " + token);
            }
        } catch (Exception e) {
            LOGGER.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
