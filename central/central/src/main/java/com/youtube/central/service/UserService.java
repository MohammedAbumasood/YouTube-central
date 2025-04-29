package com.youtube.central.service;


import com.youtube.central.dto.NotificationMessage;
import com.youtube.central.dto.UserCredentialDto;
import com.youtube.central.model.AppUser;
import com.youtube.central.repository.AppUserRepo;
import com.youtube.central.security.JwUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    AppUserRepo appUserRepo;


    RabbitMqService rabbitMqService;

    @Autowired
    public UserService(AppUserRepo appUserRepo,
                       RabbitMqService rabbitMqService)
    {
        this.appUserRepo = appUserRepo;
        this.rabbitMqService = rabbitMqService;
    }

    public AppUser getUserByEmail(String email){
        return appUserRepo.findByEmail(email);
    }

    public String loginUser(UserCredentialDto credentials){
        String email = credentials.getEmail();   //H.W Validate email
        AppUser user = this.getUserByEmail(email);
        if(user.getPassword().equals(credentials.getPassword())){
            // generate Token
            String cred = user.getEmail() + ":" + user.getPassword();
            return cred;

        }
        return "Incorrect password";
    }

    public void registerUser(AppUser user)
    {
        //call repository layer to save user
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        appUserRepo.save(user);
        // we need to add notification message record in mq
        // Insert user notification message payload inside rabbitmq queue
        NotificationMessage message = new NotificationMessage();
        message.setEmail(user.getEmail());
        message.setPurpose("user_registration");
        message.setName(user.getName());
        rabbitMqService.insertMessageToQueue(message);
    }

    public AppUser getUserById(UUID userId){
        return appUserRepo.findById(userId).orElse(null);
    }
}
