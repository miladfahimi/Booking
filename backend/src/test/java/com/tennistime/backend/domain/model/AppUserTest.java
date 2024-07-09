package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser(1L, "username", "password", "email@example.com", "1234567890", "FirstName", "LastName", "USER", null);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, appUser.getId());
        assertEquals("username", appUser.getUsername());
        assertEquals("password", appUser.getPassword());
        assertEquals("email@example.com", appUser.getEmail());
        assertEquals("1234567890", appUser.getPhone());
        assertEquals("FirstName", appUser.getFirstName());
        assertEquals("LastName", appUser.getLastName());
        assertEquals("USER", appUser.getRole());
    }
}
