package com.youtube.video_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class VideoDetailRequestBody {

    String name;
    String description;
    List<String> tags;
}
