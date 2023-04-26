package com.acorn.springboardstudy.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String to; // 보내는 사람
    private String title;
    private String message;

}
