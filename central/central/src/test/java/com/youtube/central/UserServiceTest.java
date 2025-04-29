package com.youtube.central;

import com.youtube.central.dto.UserCredentialDto;
import com.youtube.central.model.AppUser;
import com.youtube.central.repository.AppUserRepo;
import com.youtube.central.service.RabbitMqService;
import com.youtube.central.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock  //Dummy Object
    AppUserRepo appUserRepo;

    @Mock
    RabbitMqService rabbitMqService;

    @InjectMocks // It Injects dummy Objects
    UserService userService;


    AppUser user;

    @BeforeEach
    void setUp(){
        //this function runs before running every Test case
        user = new AppUser();
        user.setId(UUID.randomUUID());
        user.setEmail("test@mail.com");
        user.setPassword("123456");
        user.setName("Test-User");
    }

    // +ve scenario test case -> login user
    @Test
    void testLoginUserSuccess(){
        UserCredentialDto userCredentialDto = new UserCredentialDto();
        userCredentialDto.setEmail("test@mail.com");
        userCredentialDto.setPassword("123456");
        when(appUserRepo.findByEmail("test@mail.com")).thenReturn(user);
        String actualResponse = userService.loginUser(userCredentialDto);
        String expectedResponse = "test@mail.com" + ":" +"123456";
        assertEquals(actualResponse, expectedResponse);
    }


    @Test
    void testLoginUserIncorrectPassword(){
        UserCredentialDto credentials = new UserCredentialDto();
        credentials.setEmail("test@mail.com");
        credentials.setPassword("Hello@123");
        when(appUserRepo.findByEmail("test@mail.com")).thenReturn(user);
        String actualResponse = userService.loginUser(credentials);
        String expectedResponse = "Incorrect Password";
        assertEquals(expectedResponse, actualResponse);
    }
}
