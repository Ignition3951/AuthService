package com.utk.authservice.controller;

import com.utk.authservice.dto.JwtResponseDto;
import com.utk.authservice.dto.UserInfoDto;
import com.utk.authservice.entities.RefreshToken;
import com.utk.authservice.service.JwtService;
import com.utk.authservice.service.RefreshTokenService;
import com.utk.authservice.service.UserDetailsServiceImpl;
import com.utk.authservice.util.Messages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/auth/v1/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto){
        try {
            Boolean isSignedUp = userDetailsService.signUp(userInfoDto);
            if(Boolean.FALSE.equals(isSignedUp)){
                return new ResponseEntity<>(Messages.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUserName());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUserName());
            return new ResponseEntity(JwtResponseDto.builder()
                    .accessToken(jwtToken)
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Messages.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
