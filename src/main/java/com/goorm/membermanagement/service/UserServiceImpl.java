package com.goorm.membermanagement.service;

import com.goorm.membermanagement.dao.UserDAO;
import com.goorm.membermanagement.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean register(UserDTO user) {
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false; // 이미 존재
        }
        userDAO.insertUser(user);
        return true;
    }

    @Override
    public UserDTO login(String username, String password) {
        UserDTO user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
