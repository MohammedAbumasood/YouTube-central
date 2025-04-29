package com.youtube.video_service.service;

import com.youtube.video_service.dto.IsValidDTO;
import com.youtube.video_service.dto.SecurityCredential;
import com.youtube.video_service.dto.VideoDetailsDto;
import com.youtube.video_service.util.ApiTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

// Work of this class is to call central api endpoints
@Service
public class CentralApiConnectionService {

    @Autowired
    ApiTemplate apiTemplate;

    @Autowired
    ModelMapper mapper;

    @Value("${central.api.url}")
    String centralApiUrl;

    //save video details method is going to call save video details endpoint present in central api
    public void saveVideoDetails(UUID channelId,
                                 VideoDetailsDto videoDetailsDto,
                                 String token)
    {
        // I need to call save video details endpoint declared in your channel controller of central api
        String endPoint = "/channel" + channelId.toString() + "/video/upload";
        //To make the post call -> apiUrl, endPoint, queryParams, requestBody
        Object resp = apiTemplate.makePostCall(centralApiUrl, endPoint,new HashMap<>(), videoDetailsDto, token);
    }

    public boolean isValidToken(String token){
        String endPoint = "/security/validate-token/"+token;
        Object object = apiTemplate.makeGetCall(centralApiUrl, endPoint, new HashMap<>());
        IsValidDTO resp = mapper.map(object, IsValidDTO.class);
        return resp.isSuccess();
    }

    public String getCredentialFromToken(String token){
        String endPoint = "/security/get-credential/" + token;
        Object object = apiTemplate.makeGetCall(centralApiUrl, endPoint, new HashMap<>());
        SecurityCredential securityCredential = mapper.map(object, SecurityCredential.class);
        return securityCredential.getCredential();
    }
}
