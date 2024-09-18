package com.bouguerra.dev.mappers;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.UserRepository;

@Component
public class UserMapperHelper {

    @Autowired
    private UserRepository userRepository;

    @Named("mapUserIdToUser")
    public User mapUserIdToUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
