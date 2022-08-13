package sanotestagservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        final long MAX_AGE_SECS = 3600;
        corsRegistry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "PUT", "POST", "DELETE")
                .allowedHeaders("*")
                .maxAge(MAX_AGE_SECS);
    }

}
