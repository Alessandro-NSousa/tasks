package com.tarefas.controller;

import com.tarefas.domain.user.User;
import com.tarefas.dto.*;
import com.tarefas.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO registerRequest, UriComponentsBuilder uriBuilder){
        try {
            User usuario = userService.register(registerRequest);
            var uri = uriBuilder.path("/api/auth/{id}").buildAndExpand(usuario.getId()).toUri();

            return ResponseEntity.created(uri).body(new RegisterResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getRole()));
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErroResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {

        LoginResponseDTO response = userService.login(body);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable UUID id) {
        var user = userService.detalharUser(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public Page<UserResponseDTO> getAllUsers(@PageableDefault(sort = {"nome"}) Pageable paginacao){

        return this.userService.getAllUsers(paginacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestBody UpdateUserProfileDTO dto
    ) {
        var user = userService.updateProfile(dto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody UpdatePasswordDTO dto
    ) {
        userService.updatePassword(dto);
        return ResponseEntity.noContent().build();
    }

}
