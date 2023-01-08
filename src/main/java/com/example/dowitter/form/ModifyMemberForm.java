package com.example.dowitter.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModifyMemberForm {
    @NotNull
    private Long uid;
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String rePassword;
}
