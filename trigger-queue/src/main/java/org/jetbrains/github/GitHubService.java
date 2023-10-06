package org.jetbrains.github;


import org.jetbrains.config.GitHubProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private final RestTemplate restTemplate;
    private final GitHubProperties gitHubProperties;

    public GitHubService(RestTemplate restTemplate, GitHubProperties gitHubProperties) {
        this.restTemplate = restTemplate;
        this.gitHubProperties = gitHubProperties;
    }

    public void addWebhook(String owner, String repo, String webhookUrl) {
        String apiUrl = String.format("%s/repos/%s/%s/hooks", gitHubProperties.getApiUrl(), owner, repo);
        HttpHeaders headers = createHeaders();

        String payload = "{" +
                "\"name\": \"web\"," +
                "\"active\": true," +
                "\"events\": [\"push\"]," +
                "\"config\": {\"url\": \"" + webhookUrl + "\", \"content_type\": \"json\"}" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            logger.info("Webhook added successfully for {}/{}", owner, repo);
        } catch (Exception e) {
            logger.error("Failed to add webhook for {}/{}", owner, repo, e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + gitHubProperties.getToken());
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }
}