package com.api.crudSinAyuda.dtos;

import java.io.Serializable;

public class UserResponseDTO implements Serializable {

    private Long id;
    private String firstName;
    private String email;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
