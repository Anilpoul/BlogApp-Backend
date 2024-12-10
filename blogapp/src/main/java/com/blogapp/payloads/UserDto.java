package com.blogapp.payloads;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min= 4, message = "Username must be min of 4 characters")
    private String name;

    @Email(message = "Email address is not valid!")
    @Pattern(regexp = "^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,6}$", message = "invalid email format")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 12, message = "password must be min of 3 chars and max of 12 char")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{3,12}$", message = "password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    /*Explanation of the Regex:
        1) ^(?=.*[a-z]) - Ensures at least one lowercase letter.
        2) (?=.*[A-Z]) - Ensures at least one uppercase letter.
        3) (?=.*\\d) - Ensures at least one digit.
        4) (?=.*[@$!%*?&]) - Ensures at least one special character (adjust the list if you need specific characters).
        5) [A-Za-z\\d@$!%*?&]{3,12} - Limits the total length to between 3 and 12 characters, allowing any combination of letters, digits, and special characters.*/
    private String password;

    @NotNull
    private String about;
}
