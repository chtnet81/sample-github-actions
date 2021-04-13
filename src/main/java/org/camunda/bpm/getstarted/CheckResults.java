package org.camunda.bpm.getstarted;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.getstarted.blackduck.BackduckClient;
import org.camunda.bpm.getstarted.blackduck.BlackduckConfigs;
import org.camunda.bpm.getstarted.blackduck.BlackduckProjects;
import org.camunda.bpm.getstarted.sonarqube.SonarClient;
import org.camunda.bpm.getstarted.sonarqube.SonarConfigs;
import org.camunda.bpm.getstarted.sonarqube.SonarIssue;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CheckResults {

    private final BackduckClient backduckClient;
    private final SonarClient sonarClient;
    private final BlackduckConfigs blackduckConfigs;
    private final SonarConfigs sonarConfigs;

    public CheckResults(BackduckClient backduckClient, SonarClient sonarClient, BlackduckConfigs blackduckConfigs, SonarConfigs sonarConfigs) {
        this.backduckClient = backduckClient;
        this.sonarClient = sonarClient;
        this.blackduckConfigs = blackduckConfigs;
        this.sonarConfigs = sonarConfigs;
    }

    @EventListener
    public void onHandle(ApplicationStartedEvent event) {
        readBlackDuckProjects();
        readSonarQubeResults();

        log.info("closing application");
        System.exit(0);
    }

    private void readBlackDuckProjects() {
        log.info("listing blackduck projects...");
        BlackduckProjects blackduckProjects = backduckClient.getProjects(blackduckConfigs.getProjectName());
        if (blackduckProjects != null) {
            blackduckProjects.getProjects().forEach(blackduckProject -> {
                log.info("project found: " + blackduckProject.getName());
            });
            log.info("property project value :" + blackduckConfigs.getProjectName());
        }
    }

    private void readSonarQubeResults() {
        log.info("listing sonarQube results...");

        Map<String, List<SonarIssue>> minorCodeSmells = null;
        try {
            minorCodeSmells = sonarClient.getMinorCodeSmell();
        } catch (IOException e) {
            log.error("problem occurred in reading vulnerabilites frm sonar", e);
        } catch (JSONException e) {
            log.error("problem occurred while mapping to JSON object", e);
        }
        minorCodeSmells.entrySet().forEach(sonarIssues -> {
            sonarIssues.getValue().stream().forEach(sonarIssue -> log.info(buildSonarMessage(sonarIssue)));
        });
    }

    private String buildSonarMessage(SonarIssue sonarIssue) {
        return sonarIssue.message + "(" + sonarIssue.rule + ")" + "in " + sonarIssue.component + "line: " + sonarIssue.line;
    }
}
