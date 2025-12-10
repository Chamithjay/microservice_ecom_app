package com.ecommerce.user.DTO;

import com.ecommerce.user.Model.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
