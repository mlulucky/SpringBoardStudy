package com.acorn.springboardstudy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRoomDto {
    private int crId; // pk auto_increment
    private String uId; // fk user.u_id 참조
    private String name;
    private String description; // 채팅방 설명
    private Date postTime;
    private Date updateTime;

}
