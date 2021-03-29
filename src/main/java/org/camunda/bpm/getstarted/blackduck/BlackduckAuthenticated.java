package org.camunda.bpm.getstarted.blackduck;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author theisc
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"bearerToken", "expiresInMilliseconds" })
public class BlackduckAuthenticated {

    @JsonProperty("bearerToken")
    private String bearerToken;
    @JsonProperty("expiresInMilliseconds")
    private Integer expiresInMilliseconds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bearerToken")
    public final String getBearerToken() {
        return bearerToken;
    }

    @JsonProperty("bearerToken")
    public final void setBearerToken(final String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @JsonProperty("expiresInMilliseconds")
    public final Integer getExpiresInMilliseconds() {
        return expiresInMilliseconds;
    }

    @JsonProperty("expiresInMilliseconds")
    public final void setExpiresInMilliseconds(final Integer expiresInMilliseconds) {
        this.expiresInMilliseconds = expiresInMilliseconds;
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