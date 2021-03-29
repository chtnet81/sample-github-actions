package org.camunda.bpm.getstarted.blackduck;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auto-generated by jsonschema2pojo.org (Blackduck API Version 2018.12.4)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"totalCount", "items", "appliedFilters", "_meta"})
public class BlackduckProjects {

    @JsonProperty("totalCount")
    private Integer totalCount;
    @JsonProperty("items")
    private List<BlackduckProject> items = null;
    @JsonProperty("appliedFilters")
    private List<Object> appliedFilters = null;
    @JsonProperty("_meta")
    private Object meta;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("totalCount")
    public final Integer getTotalCount() {
        return totalCount;
    }

    @JsonProperty("totalCount")
    public final void setTotalCount(final Integer totalCount) {
        this.totalCount = totalCount;
    }

    @JsonProperty("items")
    public final List<BlackduckProject> getProjects() {
        return items;
    }

    @JsonProperty("items")
    public final void setProjects(final List<BlackduckProject> items) {
        this.items = items;
    }

    @JsonProperty("appliedFilters")
    public final List<Object> getAppliedFilters() {
        return appliedFilters;
    }

    @JsonProperty("appliedFilters")
    public final void setAppliedFilters(final List<Object> appliedFilters) {
        this.appliedFilters = appliedFilters;
    }

    @JsonProperty("_meta")
    public final Object getMeta() {
        return meta;
    }

    @JsonProperty("_meta")
    public final void setMeta(final Object meta) {
        this.meta = meta;
    }

    @JsonAnyGetter
    public final Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public final void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }


}
