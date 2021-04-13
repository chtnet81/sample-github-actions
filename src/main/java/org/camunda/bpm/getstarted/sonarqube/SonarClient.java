package org.camunda.bpm.getstarted.sonarqube;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SonarClient {

    public Map<String, List<SonarIssue>> getMinorCodeSmell() throws IOException, JSONException {

        List<SonarIssue> issues = new ArrayList<>();

        String resourceApi = "http://localhost:9000/api/issues/search?s=FILE_LINE&resolved=false&severities=MINOR&types=CODE_SMELL&ps=100";
        String response = SonarHelper.getResourceFromSonar(resourceApi);

        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                JSONArray issuesJson = new JSONObject(response).getJSONArray("issues");
                issues = objectMapper.readValue(issuesJson.toString(), new TypeReference<List<SonarIssue>>() {
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        return issues.stream().filter(f -> f.type.equalsIgnoreCase("CODE_SMELL"))
                .collect(Collectors.groupingBy(i -> i.type));
    }
}
