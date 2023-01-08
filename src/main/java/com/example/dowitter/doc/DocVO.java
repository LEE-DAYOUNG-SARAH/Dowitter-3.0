package com.example.dowitter.doc;

import lombok.Data;

import java.util.Date;

@Data
public class DocVO {
    private Long uid;
    private Long memberUid;
    private String content;
    private Date regDatetime;
}
