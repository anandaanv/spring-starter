package com.erx.service.service;


import com.erx.service.model.User;
import com.erx.service.model.UserDto;
import java.util.List;

public interface UserService {

    User approve(UserDto user);

    User save(UserDto user);
    List<User> findAll();
    void delete(int id);

    User findOne(String username);

    User findById(int id);

    UserDto update(UserDto userDto);
}
