package com.youtube.central.service;

import com.youtube.central.dto.NotificationMessage;
import com.youtube.central.dto.VideoDetailsDto;
import com.youtube.central.model.AppUser;
import com.youtube.central.model.Channel;
import com.youtube.central.model.Tag;
import com.youtube.central.model.Video;
import com.youtube.central.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    ChannelService channelService;

    @Autowired
    TagService tagService;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    RabbitMqService rabbitMqService;

    String videoLink;


    public void saveVideo(Video video){
        //call Video repository to save
        videoRepo.save(video);
    }

    public void saveVideoDetails(UUID channelId, VideoDetailsDto videoDetailsDto){
        // first we need to get the channel Object
        Channel channel = channelService.getChannelById(channelId);
        Video video = new Video();
        video.setId(videoDetailsDto.getId());
        video.setVideoLink(videoDetailsDto.getVideoLink());
        video.setUpdateAt(videoDetailsDto.getUpdateAt());
        video.setUploadDateTime(videoDetailsDto.getUploadDateTime());
        // from video api we are getting List<String> tags but to create video model Object we need tags as List<Tags>
        List<String> tags = videoDetailsDto.getTags();
        //In this list we can get tags which are already register in our database or which are new ags
        //if a tag is already present in tags table then we need to get teh tag from table
        //if not present i will create a new tag inside my table
        List<Tag> dbTagList = tagService.getAllTagsFromSystem(tags);
        video.setTags(dbTagList); // Now video object is created
        //save these video details inside video table.
        this.saveVideo(video);
        this.videoLink = videoDetailsDto.getVideoLink();
        //We need to update the list videos of channel
        channel.getVideos().add(video);
        channelService.updateChannel(channel);
        //we need to notify all the subscribers that hey  new video uploaded over the channel
        this.notifySubscribers(channel.getSubscribers());

    }

    public void notifySubscribers(List<AppUser> subscribers){
        for(int i=0; i<subscribers.size(); i++){
            AppUser subscriber = subscribers.get(i);
            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setName(videoLink);
            notificationMessage.setPurpose("new_Video");
            notificationMessage.setEmail(subscriber.getEmail());
            rabbitMqService.insertMessageToQueue(notificationMessage);
        }
    }
}
