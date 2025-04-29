package com.youtube.central.controller;


import com.youtube.central.dto.IsValidDTO;
import com.youtube.central.dto.SecurityCredential;
import com.youtube.central.security.JwUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/central/security")
@Slf4j
public class SecurityController {

    @Autowired
    JwUtil jwUtil;

    @GetMapping("/validate-token/{token}")
    public IsValidDTO validateToken(@PathVariable String token){
        log.info(token);
        boolean isValid = jwUtil.isValidToken(token);
        IsValidDTO validDTO = new IsValidDTO();
        validDTO.setSuccess(String.valueOf(isValid));
        return validDTO;
    }

    @GetMapping("get-credential/{token}")
    public SecurityCredential getSecurityCredential(@PathVariable String token){
        SecurityCredential credential = new SecurityCredential();
        credential.setCredential(jwUtil.decryptToken(token));
        return credential;
    }
}
