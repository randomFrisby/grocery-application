package com.grofers.service;

import com.grofers.dto.UserDTO;
import com.grofers.exceptions.NotFoundException;
import com.grofers.model.User;

public interface UserService {
    public UserDTO register(User user) throws NotFoundException;
}
