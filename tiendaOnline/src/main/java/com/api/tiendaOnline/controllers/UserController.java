package com.api.tiendaOnline.controllers;

import com.api.tiendaOnline.models.UserModel;
import com.api.tiendaOnline.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<UserModel> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") Long id){
        return userService.getById(id)
                .map((ResponseEntity::ok))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@Valid @RequestBody UserModel user) {
        UserModel saved = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserModel> updateUserById(@Valid @PathVariable("id") Long id, @RequestBody UserModel user) {
        UserModel updated = userService.updateById(user, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        boolean ok = userService.deleteById(id);
        if (ok){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }
    }
}
