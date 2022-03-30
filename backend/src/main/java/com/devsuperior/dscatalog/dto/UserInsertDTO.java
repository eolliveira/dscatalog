package com.devsuperior.dscatalog.dto;

import javax.validation.constraints.Size;

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
