package com.kencode.jwt_spring.Service;

import com.kencode.jwt_spring.Model.User;
import com.kencode.jwt_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User getById(int id) {
        return this.userRepository.findById(id);
    }
}
