package com.goorm.membermanagement.dao;

import com.goorm.membermanagement.dto.UserDTO;

import java.util.List;

public interface UserDAO {
    void insertUser(UserDTO user);
    UserDTO findByUsername(String username);
    List<UserDTO> findAll();
}
