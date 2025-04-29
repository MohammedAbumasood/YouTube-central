package com.youtube.central.controller;


import com.youtube.central.dto.UserCredentialDto;
import com.youtube.central.model.AppUser;
import com.youtube.central.security.JwUtil;
import com.youtube.central.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/central/user")
public class UserController {
    UserService userService;

    @Autowired
    JwUtil jwUtil;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody AppUser user)
    {
        userService.registerUser(user);
        String credentials = user.getEmail()+ ":" + user.getPassword();
        return jwUtil.generateToken(credentials);
    }

    @GetMapping("/login")
    public ResponseEntity loginUser (@RequestBody UserCredentialDto credentials){
        String resp = userService.loginUser(credentials);
        if(resp.equals("Incorrect password")){
            return new ResponseEntity("Incorrect password", HttpStatus.UNAUTHORIZED);
        }else{
            String token = jwUtil.generateToken(resp);
            return new ResponseEntity(resp, HttpStatus.OK);
        }
    }
}
