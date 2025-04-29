package com.youtube.notification.api.controller;


import com.rabbitmq.client.Channel;
import com.youtube.notification.api.dto.NotificationMessage;
import com.youtube.notification.api.enums.NotificationType;
import com.youtube.notification.api.service.CommonUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommonController {

    @Autowired
    CommonUserService commonUserService;

    @RabbitListener(queues = "notification-queue", ackMode = "MANUAL")
    public void consumeMessage(@Payload NotificationMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception
    {
        // it will run at the point if time when some notification message payload is inserted inside
        //Notification-queue and as the listner is subscribed to the queue automatically get triggered

        log.info("Notification Message received successfully payload = "+message.toString());
        try{
            if(message.getPurpose().equals(NotificationType.user_registration.toString()))
            {
                log.info("Calling user service to send registration mail");
                commonUserService.sendUserRegistrationEmail(message);
                channel.basicAck(tag, true);
            }
            else if(message.getPurpose().equals(NotificationType.subscriber_added.toString()))
            {
                log.info("calling the commonUserService to send subscriber added mail to the user");
                commonUserService.sendSubscriberAddedMail(message);
                channel.basicAck(tag, true);
            }
            else if(message.getPurpose().equals(NotificationType.create_channel.toString())){
                log.info("common controller: Type of notification is create_channel calling commonuserservice");
                commonUserService.sendCreateChannelNotification(message);
                channel.basicAck(tag, true);
            }else if(message.getPurpose().equals(NotificationType.new_video.toString())){
                log.info("got type of message as new_video");
                commonUserService.notifyNewVideoUploadedToSubscriber((message));
                channel.basicAck(tag, true);
            }
        }catch(Exception e)
        {
            log.error(e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
