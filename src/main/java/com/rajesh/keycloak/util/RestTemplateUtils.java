package com.rajesh.keycloak.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestTemplateUtils {

    @Autowired
    RestTemplate restTemplate;

    public <T> List<T> getApi(final String path, final HttpMethod method, HttpEntity httpEntity) {
        final ResponseEntity<List<T>> response = restTemplate.exchange(
                path,
                method,
                httpEntity,
                new ParameterizedTypeReference<List<T>>(){});
        List<T> list = response.getBody();
        return list;
    }

}
