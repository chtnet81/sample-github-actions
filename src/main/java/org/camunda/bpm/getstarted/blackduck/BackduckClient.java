package org.camunda.bpm.getstarted.blackduck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 *  REST Client for Blackduck API for testing purpose. The goal is to use this class from GitHub actions
 *
 */
@Component
@Slf4j
public class BackduckClient {
    private static final String PATH_AUTHENTICATION = "/api/tokens/authenticate";
    private static final String PATH_PROJECTS = "/api/projects";
    private static final String PATH_PROJECT_VERSIONS = "/api/projects/{0}/versions";
    private static final String PATH_PROJECT_VERSION_COMPONENTS = "/api/projects/{0}/versions/{1}/components";
    private static final String PATH_PROJECT_VERSION_COMPONENTS_V2 = "/api/v1/releases/{0}/component-bom-entries";
    private static final String PATH_PROJECT_TAGS = "/api/projects/{0}/tags";
    private static final String PATH_USERS = "/api/users";
    private static final String PATH_ROLES = "/api/roles";
    private static final String PATH_POLICY_STATUS = "/api/projects/{0}/versions/{1}/components/{2}/versions/{3}/policy-status";


    @Value("${blackduck.url}")
    private String serverUrl;
    @Value("${blackduck.token}")
    private String token;
    private RestTemplate restTemplate;
    private BlackduckAuthenticated authentication;

    public BackduckClient() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        this.restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        this.restTemplate.setMessageConverters(messageConverters);

    }

    @EventListener
    public void onHandle(ApplicationStartedEvent event) {
        log.info("listing blackduck projects...");
        BlackduckProjects blackduckProjects = getProjects("AK-github-actions");
        blackduckProjects.getProjects().forEach(blackduckProject -> {
            log.info("project found: " + blackduckProject.getName());
        });

        // shutdown Spring app
        System.exit(0);
    }

    private BlackduckProjects getProjects(final String filterProjectname) {
        loginToBlackDuckServer();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String token = this.authentication.getBearerToken();
            headers.add("Authorization", "Bearer " + token);

            HttpEntity entity = new HttpEntity(headers);
            String projectFilter = "";
            if (filterProjectname != null) {
                projectFilter = "&q=name:" + filterProjectname;
            }
            ResponseEntity<BlackduckProjects> response = this.restTemplate
                    .exchange(this.serverUrl + PATH_PROJECTS + "?limit=100000" + projectFilter, HttpMethod.GET, entity, BlackduckProjects.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                return null;
            } else {
                log.info("error");

            }
        } catch (RestClientException e) {
            throw new RuntimeException(e.getMessage(), e);

        }

        return null;
    }


    private void loginToBlackDuckServer() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "token " + this.token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity entity = new HttpEntity(headers);
            this.authentication = null;
            log.debug("Blackduck authentication ...");
            ResponseEntity<BlackduckAuthenticated> response =
                    this.restTemplate.exchange(this.serverUrl + PATH_AUTHENTICATION, HttpMethod.POST, entity, BlackduckAuthenticated.class);
            this.authentication = response.getBody();
            if (this.authentication == null || this.authentication.getBearerToken() == null
                    || this.authentication.getBearerToken().isEmpty()) {
                throw new RuntimeException("Internal authentication exception");
            }
        } catch (RestClientException e) {
            if (e instanceof ResourceAccessException && e.getCause() != null && e.getCause() instanceof ConnectException) {
                throw new RuntimeException(e.getMessage(), e);
            }
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException hcee = (HttpClientErrorException) e;
                if (hcee.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    log.info("user unauthorized to login");
                }
            }

        }
    }

}
