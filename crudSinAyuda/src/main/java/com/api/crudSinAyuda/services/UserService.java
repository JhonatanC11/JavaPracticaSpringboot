package com.api.crudSinAyuda.services;

import com.api.crudSinAyuda.dtos.UserRequestDTO;
import com.api.crudSinAyuda.dtos.UserResponseDTO;
import com.api.crudSinAyuda.exceptions.EmailAlreadyExistsException;
import com.api.crudSinAyuda.exceptions.UserNotFoundException;
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

    public List<UserResponseDTO> getUsers(){
        List<UserModel> users = userRepository.findAll();
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponseDTO saveUser(UserRequestDTO userDto){
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }
        UserModel user = toEntity(userDto);
        UserModel savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public Optional<UserResponseDTO> getById(Long id){
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    public UserResponseDTO updateById(UserRequestDTO request, Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Usuario con id " + id + " no encontrado"));

        Optional<UserModel> emailOwner = userRepository.findByEmail(request.getEmail());
        if (emailOwner.isPresent() && !emailOwner.get().getId().equals(id)){
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName((request.getLastName()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return toResponse(userRepository.save(user));
    }

    public boolean deleteById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UserModel toEntity(UserRequestDTO dto) {
        UserModel user = new UserModel();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        return user;
    }

    private UserResponseDTO toResponse(UserModel user){
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setEmail(user.getEmail());

        return response;
    }
}
