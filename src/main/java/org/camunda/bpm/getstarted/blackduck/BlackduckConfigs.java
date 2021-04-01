package org.camunda.bpm.getstarted.blackduck;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BlackduckConfigs {

    @Value("${project.name}")
    private String projectName;

    @Value("${project.version}")
    private String projectVersion;

    @Value("${blackduck.url}")
    private String serverUrl;

    @Value("${blackduck.token}")
    private String token;
}
