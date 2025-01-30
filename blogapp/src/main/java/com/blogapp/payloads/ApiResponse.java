package com.blogapp.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    ApiResponse {
    private String message;
    private boolean success;
    private String token;

    public ApiResponse(String message, boolean status){
        this.message = message;
        this.success= status;
    }

}
