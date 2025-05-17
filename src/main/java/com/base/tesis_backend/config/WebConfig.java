package com.base.tesis_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Esto es una clase configuracion para que el backend acepte llamadas desde el
//puerto 4200 (angular) en localhost (es para development)
@Configuration
public class WebConfig {
    /*
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); //esto es para permitir recibir el token por el headaer

            }
        };
    }
    */
    //es un bean para que pueda injectar restTemplate en cualquier lado
    //restTemplate lo uso para conectarme a la api de TMDB
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
