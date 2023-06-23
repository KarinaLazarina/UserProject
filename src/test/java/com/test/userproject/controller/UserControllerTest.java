package com.test.userproject.controller;

import com.test.userproject.exception.EmailDuplicationException;
import com.test.userproject.exception.UserNotFoundException;
import com.test.userproject.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.test.userproject.util.TestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Test
    public void getUsersTest() throws Exception {
        //given
        var users = List.of(createUserWithTestData(), createUserWithTestData());
        //when
        when(service.getUsers()).thenReturn(users);
        //then
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        //given
        var user = createUserWithTestData();
        //when
        when(service.getUserById(any())).thenReturn(user);
        //then
        mvc.perform(get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    public void getUserByIdWheUserNotFoundTest() throws Exception {
        //when
        when(service.getUserById(any())).thenThrow(UserNotFoundException.class);
        //then
        mvc.perform(get("/users/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUserTest() throws Exception {
        //given
        var userDTO = createUserDTOAsString();
        var user = createUserWithTestData();
        //when
        when(service.createUser(any())).thenReturn(user);
        //then
        mvc.perform(post("/users")
                        .content(userDTO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(TEST_EMAIL)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)));
    }

    @Test
    public void createUserWhenEmailDuplicationTest() throws Exception {
        //given
        var userDTO = createUserDTOAsString();
        //when
        when(service.createUser(any())).thenThrow(EmailDuplicationException.class);
        //then
        mvc.perform(post("/users")
                        .content(userDTO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserTest() throws Exception {
        //given
        var updatedName = "updated name";
        var userDTO = createUserDTOAsString();
        var user = createUserWithTestData();
        user.setName(updatedName);
        //when
        when(service.updateUser(any(), any())).thenReturn(user);
        //then
        mvc.perform(put("/users/" + user.getId())
                        .content(userDTO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(TEST_EMAIL)))
                .andExpect(jsonPath("$.name", is(updatedName)));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
