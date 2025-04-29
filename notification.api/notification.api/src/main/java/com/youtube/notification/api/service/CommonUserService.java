package com.youtube.notification.api.service;


import com.youtube.notification.api.dto.NotificationMessage;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class CommonUserService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    MailService mailService;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${youtube.platform.name}")
    String platformName;

    @Value("${spring.mail.host}")
    private String mailHost;


    public void sendUserRegistrationEmail(NotificationMessage notificationMessage) throws Exception
    {
        // This function will send registration email to the user
        // so, email is of type html so we need to get html template
        //Before getting html template we need to create variables inside html template
        log.info("Inside common user Service: "+ mailHost);
        Context context = new Context();
        context.setVariable("userName", notificationMessage.getName());
        context.setVariable("platformName", platformName);

        // we need to get html template inform of string and all the variable populated inside html template
        // TemplateEngine.process will insert values for all the variables defined inside html template
        String htmlEmailContent = templateEngine.process("user-registration-email", context);

        // I need to set html content inside MimeMessage
        log.info("Email template loaded: "+htmlEmailContent);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(notificationMessage.getEmail());
        mimeMessageHelper.setSubject("Welcome to YouTube");
        mimeMessageHelper.setText(htmlEmailContent, true);

        log.info("MimeMessage created calling mail service to send mail");
        mailService.sendEmail(mimeMessage);
    }

    public void sendCreateChannelNotification(NotificationMessage message) throws Exception{
        log.info("commonUserService: inside sendCreateChannelNotification method");

        Context context = new Context();
        context.setVariable("userName", message.getName());
        context.setVariable("platformName", platformName);

        String htmlEmailContent = templateEngine.process("create-channel-email", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(message.getEmail());
        mimeMessageHelper.setSubject("Channel Created Successfully");
        mimeMessageHelper.setText(htmlEmailContent, true);

        log.info("MimeMessage created calling mail service to send email");
        mailService.sendEmail(mimeMessage);
    }

    public void sendSubscriberAddedMail(NotificationMessage message) throws Exception{

        log.info("commonUserService inside sendCreateChannelNotification method");
        Context context = new Context();
        context.setVariable("channelName",message.getName());
        context.setVariable("platformName", platformName);

        String htmlTemplate = templateEngine.process("subscriber-added", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(message.getEmail());
        mimeMessageHelper.setText(htmlTemplate, true);
        mimeMessageHelper.setSubject("New Subscriber Alert!");

        log.info("MimeMessage created calling mail service to send emails");
        mailService.sendEmail(mimeMessage);

    }

    public void notifyNewVideoUploadedToSubscriber(NotificationMessage notificationMessage) throws  Exception{
        String subscriberEmail = notificationMessage.getEmail();
        String subscriberName = notificationMessage.getName();

        Context context = new Context();
        context.setVariable("subscriberName", subscriberEmail);
        context.setVariable("videoLink", notificationMessage.getName());
        context.setVariable("platformName", platformName);

        String htmlTemplate = templateEngine.process("new-video-notification", context);
        log.info(htmlTemplate);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(subscriberEmail);
        helper.setText(htmlTemplate, true);
        helper.setSubject("New Video Alert !!");
        mailService.sendEmail(mimeMessage);

    }
}
