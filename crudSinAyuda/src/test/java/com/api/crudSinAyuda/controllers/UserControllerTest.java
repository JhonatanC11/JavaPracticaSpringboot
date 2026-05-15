package com.api.crudSinAyuda.controllers;

import com.api.crudSinAyuda.exceptions.UserNotFoundException;
import com.api.crudSinAyuda.models.UserModel;
import com.api.crudSinAyuda.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setId(1L);
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        user.setEmail("juan@example.com");
        user.setPhone("123456789");
    }

    @Test
    void getUsers_always_returns200WithList() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void saveUser_withValidData_returns201() throws Exception {
        when(userService.saveUser(any())).thenReturn(user);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    void saveUser_withInvalidData_returns400() throws Exception {
        UserModel invalid = new UserModel();

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").exists());
    }

    @Test
    void getUserById_whenExists_returns200() throws Exception {
        when(userService.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getUserById_whenNotFound_returns404() throws Exception {
        when((userService.getById(99L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserById_whenNotFound_returns404() throws Exception {
        when(userService.updateById(any(),  eq(99L)))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));

        mockMvc.perform(put("/user/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletedUserById_whenExists_return200() throws Exception {
        when(userService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado"));
    }
}
