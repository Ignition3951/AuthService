package com.utk.authservice.controller;

import com.utk.authservice.dto.AuthRequestDto;
import com.utk.authservice.dto.JwtResponseDto;
import com.utk.authservice.dto.RefreshTokenRequestDto;
import com.utk.authservice.entities.RefreshToken;
import com.utk.authservice.service.JwtService;
import com.utk.authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/v1/login")
    public ResponseEntity authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(),authRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
            return new ResponseEntity(JwtResponseDto.builder()
                    .accessToken(jwtService.createToken(new HashMap<>(),authRequestDto.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        }else{
            return new ResponseEntity("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userinfo -> {
                    String accesToken = jwtService.createToken(new HashMap<>(),userinfo.getUserName());
                    return JwtResponseDto.builder()
                            .accessToken(accesToken)
                            .token(refreshTokenRequestDto.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in DB!!!!!"));
    }


}
