package com.tarefas.services;

import com.tarefas.domain.user.User;
import com.tarefas.dto.LoginRequestDTO;
import com.tarefas.dto.RegisterRequestDTO;
import com.tarefas.dto.ResponseLoginDTO;
import com.tarefas.infra.security.TokenService;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

//    public ResponseLoginDTO login2(LoginRequestDTO body) {
//        UserDetails user = userRepository.findByEmail(body.email());
//
//        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
//            throw new RuntimeException();
//        }
//
//        String token = tokenService.generateToken(user);
//        return new ResponseLoginDTO(user.getUsername(), token);
//    }

    public ResponseLoginDTO login(LoginRequestDTO body) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new  ResponseLoginDTO(auth.getName(), token);
    }

    public User register(RegisterRequestDTO body){

        if(this.userRepository.findByEmail(body.email()) != null) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(body.password());
        User newUser = new User(body.email(), encryptedPassword, body.role());

        this.userRepository.save(newUser);

        return newUser;
    }

    public User detalharUser(UUID id) {
        var usuario = this.userRepository.getReferenceById(id);
        return usuario;
    }
}
