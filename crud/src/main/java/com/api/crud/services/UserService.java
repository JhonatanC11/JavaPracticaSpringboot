package com.api.crud.services;

import com.api.crud.models.UserModel;
import com.api.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Fallback;
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

    public UserModel saveUser (UserModel user){
        return userRepository.save(user);
    }

    public Optional<UserModel> getById (Long id){
        return userRepository.findById(id);
    }

    public UserModel updateById (UserModel request, Long id){
        UserModel user = userRepository.findById(id).get();
        user.setFirstname(request.getFirstname());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public boolean deleteUser(Long id){
        try{
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
