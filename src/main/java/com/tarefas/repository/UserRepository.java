package com.tarefas.repository;

import com.tarefas.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    Page<User> findAllByAtivoTrue(Pageable pageable);

    Optional<User> findByIdAndAtivoTrue(UUID id);

}
