package com.youtube.video_service.util;

import jdk.jfr.RecordingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

public class ApiTemplateImpl implements  ApiTemplate{

    @Autowired
    RestTemplate restTemplate;

    //Central Api ->
    //http://localhost:8080/api/v1/central
    //endPoint : /user/register
    //{userName : "Masood", userEmail: "masood@gamil.com"}

    //finalURL :http://localhost:8080/api/v1/central/user/register?userName=Masood&userEmail=masood@gamil.com


    public String addQueryParams(String url, Map<String, String > queryParams){
        if(queryParams.size() == 0){
            return url;
        }
        int count = 1;
        url += "?";
        for(String Key : queryParams.keySet()){
            url += Key + "=" + queryParams.get(Key);
            if(count < queryParams.size()){
                url += "&";
            }
            count++;
        }
        return url;
    }

    public URI createUrl(String apiUrl, String endPoint, Map<String, String> queryParams){
        String url = apiUrl + endPoint;
        url = this.addQueryParams(url, queryParams);
        return URI.create(url);
    }

    @Override
    public Object makeGetCall(String apiUrl, String endPoint, Map<String, String> queryParams) {
        //First step is to create url
        URI finalUrl = this.createUrl(apiUrl, endPoint, queryParams);
        //second step is to create RequestEntity
        RequestEntity request = RequestEntity.get(finalUrl).build();
        //After creating request i need to hit the request
        // so, to hit the request we need a library that is restTemplate
        ResponseEntity<Object> response = restTemplate.exchange(finalUrl, HttpMethod.GET, request, Object.class);
        return response.getBody();
    }

    @Override
    public Object makePostCall(String apiUrl, String endPoint, Map<String, String> queryParams, Object requestBody, String token) {
        URI url = this.createUrl(apiUrl, endPoint, queryParams);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + token);
        RequestEntity request = RequestEntity.post(url).body(requestBody);
        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
        return response.getBody();
    }

    public Object makePutCall(String apiUrl,String endPoint, Map<String, String> queryParams, Object requestBody){
        URI finalUrl = this.createUrl(apiUrl, endPoint, queryParams);
        RequestEntity request = RequestEntity.put(finalUrl).body(requestBody);
        ResponseEntity<Object> response = restTemplate.exchange(finalUrl, HttpMethod.PUT, request, Object.class);
        return response.getBody();
    }
}
