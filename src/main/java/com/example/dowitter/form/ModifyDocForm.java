package com.example.dowitter.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModifyDocForm {
    @NotNull
    private Long uid;

    @NotBlank
    private String content;
}
