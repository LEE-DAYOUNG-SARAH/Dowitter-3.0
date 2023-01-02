package com.example.dowitter.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JoinForm {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String rePassword;
}
