package com.devsuperior.dscatalog.components.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

import javax.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @Size(min = 4)
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
