package com.caam.mrs.api.rest;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service("springRestClient")
public class SpringRestClient implements RESTClient {
 
    private RestTemplate restTemplate;
 
    @Autowired
    public SpringRestClient(RestTemplateBuilder builder) {
        this.restTemplate =  builder.build();
    }
 
    @Override
    public Object post(String resourceUrl, MultiValueMap<String, String> request, String username, String password) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request,
                createHeadersWithUsernamePassword(username, password));
        ResponseEntity<String> response = this.restTemplate.exchange(resourceUrl, HttpMethod.POST, requestEntity, String.class);
        Object postObject = response.getBody();
        return postObject;
    }
 
    private HttpHeaders createHeadersWithUsernamePassword(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Accept", MediaType.APPLICATION_JSON.toString());
                set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
                set("Authorization", authHeader);
            }
        };
    }
}