package ru.semykin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }
}
