package com.youtube.central.security;

import com.youtube.central.model.AppUser;
import com.youtube.central.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwUtil {

    @Autowired
    UserService userService;

    @Value("${central.security.secret.key}")
    String secretKey;

    Long expirationTime = 1200000L;

    //create JWT Token on the basis of credential
    //As i mentioned in jwt token you are encrypting some information
    //what information we are going to encrypt we are going to encrypt user credential
    // credentials = masood@gmail.com:12345
    // we got the credential generate Token function we will encrypt credential with the help of algorithm

    public String generateToken(String credentials){
        String jwtToken = Jwts.builder().setSubject(credentials)
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return jwtToken;
    }

    public String decryptToken(String token){
        String credentials = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return credentials;
    }

    public boolean isValidToken(String token){
        //encrypt token
        String credentials = this.decryptToken(token);
        String email = credentials.split(":")[0];
        String password = credentials.split(":")[1];
        //validate this token is correct  or not
        AppUser user = userService.getUserByEmail(email);
        if(user == null){
            return false;
        }
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }
}