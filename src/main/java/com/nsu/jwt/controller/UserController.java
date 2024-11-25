package com.nsu.jwt.controller;

import com.nsu.jwt.config.JwtUtils;
import com.nsu.jwt.model.AuthenticationRequest;
import com.nsu.jwt.model.AuthenticationResponse;
import com.nsu.jwt.model.User;
import com.nsu.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?>saveUser (@RequestBody User user){
        User save = userService.save(user);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllUser(){
        List<User> all = userService.findAll();
        return ResponseEntity.ok(all);
    }
}
