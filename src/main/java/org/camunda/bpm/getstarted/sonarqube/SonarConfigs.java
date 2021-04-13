package org.camunda.bpm.getstarted.sonarqube;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SonarConfigs {

    @Value("${project.name}")
    private String projectName;
}
