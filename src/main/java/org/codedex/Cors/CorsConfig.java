package org.codedex.Cors;

//? Behöver en config av cors så att jag kommer åt get metoden
//? Från min react hemsida

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        //? Tillåter ankomst så länga den kommer från localhost
                        .allowedOriginPatterns("http://localhost:*")
                        //? Ger bara klienten rättigheter att hämta från databasen
                        //? Den kommer inte att kunna göra några ändringar

                        .allowedMethods("GET");
            }
        };
    }
}
