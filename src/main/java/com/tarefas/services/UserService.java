package com.tarefas.services;

import com.tarefas.domain.user.User;
import com.tarefas.dto.LoginRequestDTO;
import com.tarefas.dto.RegisterRequestDTO;
import com.tarefas.dto.ResponseDTO;
import com.tarefas.infra.security.TokenService;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public ResponseDTO login(LoginRequestDTO body) {
        User user = repository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = tokenService.generateToken(user);
        return new ResponseDTO(user.getEmail(), token);
    }

    public ResponseDTO register(RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return new ResponseDTO(newUser.getEmail(), token);
        }
        return null;
    }
}
