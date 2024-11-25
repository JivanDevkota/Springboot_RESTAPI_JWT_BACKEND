package com.nsu.jwt.controller;

import com.nsu.jwt.config.JwtUtils;
import com.nsu.jwt.model.AuthenticationRequest;
import com.nsu.jwt.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> generateToken
            (@RequestBody AuthenticationRequest authenticationRequest){
        Authentication authenticate = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtUtils.generateToken(authenticate.getName());
        System.out.println(token);
        System.out.println("Authorities: " + authenticate.getAuthorities());

        AuthenticationResponse response = new AuthenticationResponse(token,"successful");
        return ResponseEntity.ok(response);
    }
}
