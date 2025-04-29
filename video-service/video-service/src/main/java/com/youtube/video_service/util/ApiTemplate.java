package com.youtube.video_service.util;

import java.util.Map;

public interface ApiTemplate{

    //http://localhost:8080/api/v1/central/user/registration?userName=Masood&userEmail=Masood@gmail.com
    public Object makeGetCall(String apiUrl, String endPoint, Map<String, String> queryParams);

    public Object makePostCall(String apiUrl, String endPoint, Map<String, String> queryParams, Object requestBody, String token);

    public Object makePutCall(String apiUrl, String endPoint, Map<String, String> queryParams, Object requestBody);

}
