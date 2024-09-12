package com.Icwd.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Icwd.user.service.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>

{
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
