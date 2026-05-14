package com.api.crudSinAyuda.controllers;

import com.api.crudSinAyuda.dtos.UserRequestDTO;
import com.api.crudSinAyuda.dtos.UserResponseDTO;
import com.api.crudSinAyuda.models.UserModel;
import com.api.crudSinAyuda.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<UserResponseDTO> getUsers(){
        return this.userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserRequestDTO user){
        UserResponseDTO saved = this.userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id){
        return this.userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(@Valid @RequestBody UserRequestDTO request, @PathVariable("id") Long id){
            UserResponseDTO updated = this.userService.updateById(request, id);
            return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id){
        boolean ok = this.userService.deleteById(id);

        if(ok){
            return ResponseEntity.ok("Usuario eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }
    }
}
