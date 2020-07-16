package com.diffreviewer.service;

import com.diffreviewer.entities.User;

import java.util.Optional;

public interface UserCRUD {
    Optional<User> getUserById(Long id);

    User getUserByUsername(String username);
}
