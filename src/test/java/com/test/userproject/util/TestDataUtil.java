package com.test.userproject.util;

import com.test.userproject.dto.UserDTO;
import com.test.userproject.model.User;

public class TestDataUtil {
    public static final String TEST_ID = "64940d43fd3cb24cba138663";
    public static final String TEST_NAME = "Name";
    public static final String TEST_EMAIL = "name@gmail.com";

    public static User createUserWithTestData() {
        return new User(TEST_ID, TEST_NAME, TEST_EMAIL);
    }

    public static String createUserDTOAsString() {
        return String.format("{\"name\":\"%s\", \"email\":\"%s\"}", TEST_NAME, TEST_EMAIL);
    }

    public static UserDTO createUserDTOWithTestData() {
        return new UserDTO(TEST_NAME, TEST_EMAIL);
    }
}
