package org.camunda.bpm.getstarted.sonarqube;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SonarIssue {

	@JsonProperty("key")
	public String key;
	@JsonProperty("rule")
	public String rule;
	@JsonProperty("severity")
	public String severity;
	@JsonProperty("component")
	public String component;
	@JsonProperty("project")
	public String project;
	@JsonProperty("line")
	public Integer line;
	@JsonProperty("hash")
	public String hash;
	@JsonProperty("flows")
	public List<Object> flows = null;
	@JsonProperty("status")
	public String status;
	@JsonProperty("message")
	public String message;
	@JsonProperty("effort")
	public String effort;
	@JsonProperty("debt")
	public String debt;
	@JsonProperty("tags")
	public List<String> tags = null;
	@JsonProperty("creationDate")
	public String creationDate;
	@JsonProperty("updateDate")
	public String updateDate;
	@JsonProperty("type")
	public String type;
	@JsonProperty("organization")
	public String organization;
	@JsonProperty("scope")
	public String scope;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
		
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}