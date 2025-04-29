package com.youtube.video_service.service;

import com.youtube.video_service.dto.VideoDetail;
import com.youtube.video_service.dto.VideoDetailRequestBody;
import com.youtube.video_service.dto.VideoDetailsDto;
import com.youtube.video_service.exception.InvalidFileType;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.models.results.ResultCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UploadService {

    @Autowired
    ImageKit imageKit;

    @Autowired
    CentralApiConnectionService centralApiConnectionService;

    public boolean isVideoFile(MultipartFile file){
        boolean res = file.getContentType().startsWith("video/");
        return res;
    }

    public VideoDetail uploadVideo(MultipartFile video,
                                   UUID channelId,
                                   VideoDetailRequestBody videoDetails,
                                   String token) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        //We need to validate file is having what kind of type
        boolean isVideo = isVideoFile(video);
        if(!isVideo){
            throw new InvalidFileType("File uploaded is not video");
        }

        // if file is a type of we need to convert it to bytes
        byte [] videoBytes = video.getBytes();
        //we need to create one request which will upload it to imageKit.io
        FileCreateRequest videoRequest = new FileCreateRequest(videoBytes, video.getOriginalFilename());
        videoRequest.setUseUniqueFileName(true);
        Result result = imageKit.upload(videoRequest);// by this line video will uploads over image kit server
        //Then get the Id & Url from the imageKit
        String videoId = result.getFileId();
        String videoUrl = result.getUrl();
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setVideoId(videoId);
        videoDetail.setVideoUrl(videoUrl);

        // We need to make call to the central Api to save video detail in video detail table
        VideoDetailsDto videoDetailsDto = new VideoDetailsDto();
        videoDetailsDto.setVideoLink(videoUrl);
        videoDetailsDto.setId(videoId);
        videoDetailsDto.setTags(videoDetails.getTags());
        videoDetailsDto.setUploadDateTime(LocalDateTime.now());
        videoDetailsDto.setUpdateAt(LocalDateTime.now());
        videoDetailsDto.setName(videoDetails.getName());
        videoDetailsDto.setDescription(videoDetails.getDescription());
        centralApiConnectionService.saveVideoDetails(channelId, videoDetailsDto, token);

        return videoDetail;
    }
}
