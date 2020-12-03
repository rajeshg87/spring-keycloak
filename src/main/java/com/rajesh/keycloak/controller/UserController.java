package com.rajesh.keycloak.controller;

import com.rajesh.keycloak.model.User;
import com.rajesh.keycloak.util.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private static final String url = "http://localhost:8082/users/";

    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    RestTemplateUtils restTemplateUtils;

    @Autowired
    WebClient webClient;

    @GetMapping(value = "/webclient/users")
    public String getUsersUsingWebclient(Model model){

        List<User> users = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<User>>() {
                })
                .block();

        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model){

        String jwtAccessToken = getJwtToken();

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "+jwtAccessToken);

        HttpEntity<String> entity=new HttpEntity<>(httpHeaders);

        List<User> users = restTemplateUtils.getApi(url, HttpMethod.GET, entity);

        model.addAttribute("users", users);
        return "users";
    }

    private String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient oAuth2Client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());

        String jwtAccessToken = oAuth2Client.getAccessToken().getTokenValue();

        System.out.println("Access Token : "+jwtAccessToken);
        return jwtAccessToken;
    }

}
