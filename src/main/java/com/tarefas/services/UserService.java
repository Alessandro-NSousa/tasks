package com.tarefas.services;

import com.tarefas.domain.user.User;
import com.tarefas.dto.*;
import com.tarefas.infra.security.TokenService;
import com.tarefas.mapper.UserMapper;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private UserMapper mapper;
    @Autowired
    private LogService logService;

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

    public UserResponseDTO detalharUser(UUID id) {
        var usuario = this.userRepository.getReferenceById(id);
        return mapper.UserToUserResponseDTO(usuario);
    }

    public void deleteUser(UUID id) {
        var usuario = userRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuário nao encontrado ou já excluído"));

        usuario.setAtivo(false);
        userRepository.save(usuario);

        var usuarioLogado = getUsuarioLogado();
        var log = new LogRequestDTO(
                "O usuário " + usuarioLogado.getUsername() + " (ID: " + usuarioLogado.getId() +
                ") excluiu o usuário '" + usuario.getNome() + "'.",
                User.class.getSimpleName(),
                usuarioLogado.getId()
        );
        logService.RegistrarLog(log);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable paginacao) {
        return userRepository.findAllByAtivoTrue(paginacao).map(mapper::UserToUserResponseDTO);
    }

    public UserResponseDTO updateProfile(UpdateUserProfileDTO dto) {

        User usuario = getUsuarioLogado();

        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }

        if (dto.username() != null) {
            usuario.setEmail(dto.username());
        }

        userRepository.save(usuario);

        logService.RegistrarLog(new LogRequestDTO(
                "Usuário atualizou seu perfil",
                User.class.getSimpleName(),
                usuario.getId()
        ));

        return mapper.UserToUserResponseDTO(usuario);
    }

    public void updatePassword(UpdatePasswordDTO dto) {

        User usuario = getUsuarioLogado();

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getPassword())) {
            throw new RuntimeException("Senha atual inválida.");
        }

        if (!dto.novaSenha().equals(dto.confirmacaoSenha())) {
            throw new RuntimeException("Nova senha e confirmação não conferem.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dto.novaSenha());
        usuario.setPassword(novaSenhaCriptografada);

        userRepository.save(usuario);

        logService.RegistrarLog(new LogRequestDTO(
                "Usuário alterou sua senha",
                User.class.getSimpleName(),
                usuario.getId()
        ));
    }

    public User getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameLogado = authentication.getName();

        // Buscar o usuário logado no banco (se necessário)
        User usuarioLogado = userRepository.findByEmail(usernameLogado);

        return usuarioLogado;
    }
}
