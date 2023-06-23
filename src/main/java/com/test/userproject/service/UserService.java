package com.test.userproject.service;

import com.test.userproject.dto.UserDTO;
import com.test.userproject.exception.EmailDuplicationException;
import com.test.userproject.exception.UserNotFoundException;
import com.test.userproject.model.User;
import com.test.userproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailDuplicationException(userDTO.getEmail());
        }
        return saveToDB(userDTO, new User());
    }

    public User updateUser(String id, UserDTO userDTO) {
        var user = getUserById(id);
        return saveToDB(userDTO, user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    private void mapUserDTOToUser(UserDTO userDTO, User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
    }

    private User saveToDB(UserDTO userDTO, User user) {
        mapUserDTOToUser(userDTO, user);
        return userRepository.save(user);
    }

}
