package com.api.tiendaOnline.services;

import com.api.tiendaOnline.exceptions.EmailAlreadyExistsException;
import com.api.tiendaOnline.exceptions.UserNotFoundException;
import com.api.tiendaOnline.models.UserModel;
import com.api.tiendaOnline.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<UserModel> getUsers(){
        return userRepository.findAll();
    }

    public Optional<UserModel> getById(Long id){
        return userRepository.findById(id);
    }

    public UserModel saveUser(UserModel user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }
        return userRepository.save(user);
    }

    public UserModel updateById(UserModel request, Long id){
        UserModel user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("Usuario con id " + id + " no encontrado"));

        Optional<UserModel> emailOwner = userRepository.findByEmail(user.getEmail());
        if (emailOwner.isPresent() && !emailOwner.get().getId().equals(id)) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public boolean deleteById(Long id){
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }
}
