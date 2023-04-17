package com.acorn.springboardstudy.dto;


import lombok.Data;

@Data
public class BoardHashTagDto {
    private int bhId; // pk generate key(== auto increment)
    private int bId; // fk boards.b_id
    private String tag; // fk hashtags.tag

}
