package com.tarefas.services;

import com.tarefas.domain.user.User;
import com.tarefas.dto.LoginRequestDTO;
import com.tarefas.dto.RegisterRequestDTO;
import com.tarefas.dto.LoginResponseDTO;
import com.tarefas.infra.security.TokenService;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public LoginResponseDTO login(LoginRequestDTO body) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(auth.getName(), token);
    }

    public User register(RegisterRequestDTO dados){

        if(this.userRepository.findByEmail(dados.email()) != null) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(dados.password());
        User newUser = new User(dados, encryptedPassword);

        this.userRepository.save(newUser);

        return newUser;
    }

    public User detalharUser(UUID id) {
        var usuario = this.userRepository.getReferenceById(id);
        return usuario;
    }

    public User getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameLogado = authentication.getName();

        // Buscar o usuário logado no banco (se necessário)
        User usuarioLogado = userRepository.findByEmail(usernameLogado);

        return usuarioLogado;
    }
}
