package com.example.dowitter.form;

import lombok.Data;

import java.util.Date;

@Data
public class DocForm {
    private Long uid;
    private Long memberUid;
    private String content;
    private Date regDatetime;
    private String userId;
}
