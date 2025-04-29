package com.youtube.central.service;

import com.youtube.central.dto.CreateChannelRequestBody;
import com.youtube.central.dto.NotificationMessage;
import com.youtube.central.exceptions.ChannelNotFound;
import com.youtube.central.exceptions.UserNotFound;
import com.youtube.central.model.AppUser;
import com.youtube.central.model.Channel;
import com.youtube.central.repository.ChannelRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ChannelService {

    @Autowired
    UserService userService;

    @Autowired
    ChannelRepo channelRepo;

    @Autowired
    RabbitMqService rabbitMqService;

    public Channel getChannelById(UUID channelId){
        return channelRepo.findById(channelId).orElse(null);
    }

    public void  updateChannel(Channel channel){
        channelRepo.save(channel);
    }

    public void createChannel(CreateChannelRequestBody channelDetails) throws UserNotFound {
        String email = channelDetails.getUserEmail();
        //we need to check with this email user is present inside database or not
        AppUser user = userService.getUserByEmail(email);
        // Now we need t validate it
        if(user == null)
        {
            //user does not exist it is wring email
            throw new UserNotFound(
                    String.format("user with email id %s does not exist in system", email)
            );
        }
        //if valid  we need to create channel inside our database
        Channel channel = new Channel();
        channel.setCreatedAt(LocalDateTime.now());
        channel.setUpdatedAt(LocalDateTime.now());
        channel.setMonetized(false);
        channel.setUser(user);
        channel.setDescription(channel.getDescription());
        channel.setName(channelDetails.getChannelName());

        // call my repo layer which will save channel inside channel table
        channelRepo.save(channel);

        // Notify user hey we have created a channel for you in your system
        // we need to mail user
        // so to mail user we need to upload notification message at our queue
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setName(user.getName());
        notificationMessage.setPurpose("create_channel");
        notificationMessage.setEmail(user.getEmail());

        //insert the code into the queue
        rabbitMqService.insertMessageToQueue(notificationMessage);
    }
    
    public void addSubscriber(UUID userId, UUID channelId){
        // I need to validate both the userId and channelId

        AppUser user = userService.getUserById(userId);
        // we are checking userId is present in our system or not
        if(user == null)
        {
            throw new UserNotFound(String.format("User with id %s doest not exist in the system"));
        }

        // we need to check channelId is present in our system or not
        Channel channel = this.getChannelById(channelId);
        if(channel == null)
        {
            // That means channel does not exist hear
            throw new ChannelNotFound(String.format("Channel with channelId %s does not exist in our system"));
        }

        List<AppUser> subscribers = channel.getSubscribers();
        subscribers.add(user);

        // I have updated list of subscribers for a particular channel Object
        // what ever i have updated i need to save this changes in the database

        channelRepo.save(channel);

        // ChannelOwner should get email hey new subscriber to your channel
        // Notification Message -> i will pass this notification msg to the Messaging queue
        NotificationMessage message = new NotificationMessage();
        message.setEmail(channel.getUser().getEmail());
        message.setPurpose("subscriber_added");
        message.setName(channel.getName());

        rabbitMqService.insertMessageToQueue(message);
    }
}
