package com.acorn.springboardstudy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FollowDto {
    private int fId;
    private String fromId;
    private String toId;
    private Date followTime;


}
