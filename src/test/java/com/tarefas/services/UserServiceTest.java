package com.tarefas.services;

import com.tarefas.builder.UserDTOBuilder;
import com.tarefas.domain.user.User;
import com.tarefas.dto.LoginRequestDTO;
import com.tarefas.dto.LoginResponseDTO;
import com.tarefas.dto.UserResponseDTO;
import com.tarefas.infra.security.TokenService;
import com.tarefas.mapper.UserMapper;
import com.tarefas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TokenService tokenService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void WhenLoginIsValidThenItShouldReturnToken() {
        var usuarioBulider = UserDTOBuilder.builder().build().toUser();
        var loginRequestDTO = UserDTOBuilder.builder().build().buildRequestDTO();

        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        when(authenticationMock.getPrincipal()).thenReturn(usuarioBulider);
        when(authenticationMock.getName()).thenReturn(loginRequestDTO.email());
        when(tokenService.generateToken(usuarioBulider)).thenReturn("fake-jwt-token");

        LoginResponseDTO resposta = userService.login(loginRequestDTO);

        assertEquals(loginRequestDTO.email(), resposta.nome());
        assertEquals("fake-jwt-token", resposta.token());

    }

}