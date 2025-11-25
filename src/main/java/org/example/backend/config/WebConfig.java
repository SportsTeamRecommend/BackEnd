package org.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080",
                        "http://localhost:5173",
                        "https://myteamfinder.site",
                        "https://www.myteamfinder.site",
                        "https://sports-team-recommend.vercel.app")
                .allowedMethods("GET", "POST")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
