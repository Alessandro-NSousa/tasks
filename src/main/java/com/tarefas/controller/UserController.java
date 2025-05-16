package com.tarefas.controller;

import com.tarefas.domain.user.User;
import com.tarefas.dto.*;
import com.tarefas.infra.security.TokenService;
import com.tarefas.repository.UserRepository;
import com.tarefas.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO body) {
//        try {
//            ResponseLoginDTO response = userService.login(body);
//            return ResponseEntity.ok(response);
//
//        }catch (RuntimeException e){
//            return ResponseEntity.badRequest().body(new ResponseLoginDTO(e.getMessage(), null));
//        }
            ResponseLoginDTO response = userService.login(body);
            return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO registerRequest){
        try {
            User response = userService.register(registerRequest);
            return ResponseEntity.ok(new ResponseRegisterDTO(response.getId(), response.getUsername(), response.getRole()));
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseErroDTO(e.getMessage()));
        }
    }


}
