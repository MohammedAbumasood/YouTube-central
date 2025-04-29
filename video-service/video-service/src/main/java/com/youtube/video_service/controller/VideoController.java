package com.youtube.video_service.controller;


import com.youtube.video_service.dto.GeneralMessage;
import com.youtube.video_service.dto.VideoDetail;
import com.youtube.video_service.dto.VideoDetailRequestBody;
import com.youtube.video_service.exception.InvalidFileType;
import com.youtube.video_service.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    UploadService uploadService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity uploadVideo(@RequestPart("videoFile") MultipartFile video,
                                      @RequestParam UUID channelId,
                                      @RequestPart("videoDetails") VideoDetailRequestBody videoDetails,
                                      String token){
        try{
            VideoDetail videoDetail = uploadService.uploadVideo(video, channelId, videoDetails, token);
            return new ResponseEntity(videoDetail, HttpStatus.CREATED);
        }catch(InvalidFileType invalidFileType){
            GeneralMessage generalMessage = new GeneralMessage();
            generalMessage.setMessage(invalidFileType.getMessage());
            return new ResponseEntity(generalMessage, HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            GeneralMessage generalMessage = new GeneralMessage();
            generalMessage.setMessage(e.getMessage());
            return new ResponseEntity(generalMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
