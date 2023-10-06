package org.jetbrains.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "github")
@Data
public class GitHubProperties {

    private String apiUrl;
    private String token;
}
