package com.goorm.membermanagement.service;

import com.goorm.membermanagement.dto.UserDTO;

public interface UserService {
    boolean register(UserDTO user);
    UserDTO login(String username, String password);
}
