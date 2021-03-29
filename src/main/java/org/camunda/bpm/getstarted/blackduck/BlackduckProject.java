package org.camunda.bpm.getstarted.blackduck;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * The Class BlackduckProject.
 */
@Data
@ToString
public class BlackduckProject {

    private String name;
    private String description;
    private Integer projectTier;
    private Boolean projectLevelAdjustments;
    private List<String> cloneCategories;
    private Boolean customSignatureEnabled;
    private String createdAt;
    private String createdBy;
    private String createdByUser;
    private String updatedAt;
    private String updatedBy;
    private String updatedByUser;
    private String source;
    @JsonProperty("_meta")
    private Object meta;


}

