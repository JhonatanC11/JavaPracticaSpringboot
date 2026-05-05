package com.api.crudSinAyuda.services;

import com.api.crudSinAyuda.models.UserModel;
import com.api.crudSinAyuda.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<UserModel> getUsers(){
        return (List<UserModel>) userRepository.findAll();
    }

    public UserModel saveUser(UserModel user){
        return userRepository.save(user);
    }

    public Optional<UserModel> getById(Long id){
        return userRepository.findById(id);
    }

    public UserModel updateById(UserModel request, Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario con id " + id + " no encontrado"));

        user.setFirstName(request.getFirstName());
        user.setLastName((request.getLastName()));
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public boolean deleteById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
