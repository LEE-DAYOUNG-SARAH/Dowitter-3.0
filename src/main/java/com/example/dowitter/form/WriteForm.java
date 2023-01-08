package com.example.dowitter.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WriteForm {
    @NotNull
    private Long memberUid;
    @NotBlank
    private String content;
}
