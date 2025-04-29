package com.youtube.central.controller;

import com.youtube.central.dto.CreateChannelRequestBody;
import com.youtube.central.dto.VideoDetailsDto;
import com.youtube.central.service.ChannelService;
import com.youtube.central.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/central/channel")
@Slf4j
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @Autowired
    VideoService videoService;

    @PostMapping("/create")
    public void createChannel(@RequestBody CreateChannelRequestBody channelDetails)
    {
        log.info("channelController: channel details received " + channelDetails);
        // we need to call a channel service
        channelService.createChannel(channelDetails);
    }

    @PutMapping("/{channelid}/subscriber")
    public void addSubscriber(@PathVariable UUID channelId,
                              @RequestBody UUID userId){
        // we need to call the user service
        channelService.addSubscriber(userId, channelId);

    }

    @PostMapping("{channelId}/video/upload")
    public void saveVideoDetails(@RequestBody VideoDetailsDto videoDetailsDto,
                                 @PathVariable UUID channelId){
        videoService.saveVideoDetails(channelId, videoDetailsDto);
    }
}
