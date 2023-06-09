package com.kencode.jwt_spring.repository;

import com.kencode.jwt_spring.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);

    public User findById(int id);
}
