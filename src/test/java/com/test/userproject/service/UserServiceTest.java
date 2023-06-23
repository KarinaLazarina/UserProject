package com.test.userproject.service;

import com.test.userproject.dto.UserDTO;
import com.test.userproject.exception.EmailDuplicationException;
import com.test.userproject.exception.UserNotFoundException;
import com.test.userproject.model.User;
import com.test.userproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.test.userproject.util.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserTest() {
        //given
        var expectedUsers = List.of(createUserWithTestData(), createUserWithTestData());
        //when
        when(userRepository.findAll()).thenReturn(expectedUsers);
        //then
        var actualUsers = userService.getUsers();
        verify(userRepository, times(1)).findAll();
        assertEquals(expectedUsers.size(), actualUsers.size());
    }

    @Test
    public void getUserByIdTest() {
        //given
        var expectedUser = createUserWithTestData();
        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(expectedUser));
        //then
        var actualUser = userService.getUserById(TEST_ID);
        verify(userRepository, times(1)).findById(any());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }

    @Test
    public void getUserByIdWhenUserNotFoundTest() {
        //when
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        //then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(TEST_ID));
    }

    @Test
    public void createUserTest() {
        //given
        var userDTO = createUserDTOWithTestData();
        var expectedUser = createUserWithTestData();
        //when
        when(userRepository.save(any())).thenReturn(expectedUser);
        //then
        var actualUser = userService.createUser(userDTO);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(userDTO.getName(), actualUser.getName());
        assertEquals(userDTO.getEmail(), actualUser.getEmail());
    }

    @Test
    public void saveUserWhenEmailDuplicationTest() {
        //given
        var userDTO = createUserDTOWithTestData();
        //when
        when(userRepository.existsByEmail(any())).thenReturn(true);
        //then
        assertThrows(EmailDuplicationException.class, () -> userService.createUser(userDTO));
    }

    @Test
    public void updateUserTest() {
        //given
        var updatedName = "updatedName";
        var userDTO = new UserDTO(updatedName, TEST_EMAIL);
        var expectedUpdatedUser = new User(TEST_ID, updatedName, TEST_EMAIL);
        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(createUserWithTestData()));
        when(userRepository.save(any())).thenReturn(expectedUpdatedUser);
        //then
        var actualUser = userService.updateUser(TEST_ID, userDTO);
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expectedUpdatedUser.getName(), actualUser.getName());
        assertEquals(TEST_ID, actualUser.getId());
    }

}
