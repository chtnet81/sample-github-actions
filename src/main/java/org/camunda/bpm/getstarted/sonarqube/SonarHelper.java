package org.camunda.bpm.getstarted.sonarqube;

import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.camunda.commons.utils.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SonarHelper {

    public static String getResourceFromSonar(String resourceURL) {

        String auth = "admin:admin";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpGet httpGet = new HttpGet(resourceURL);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .build();) {

            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 401){
                log.error("unauthorized: unable to login sonar server");
                throw new RuntimeException("unauthorized: unable to login sonar server");
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
