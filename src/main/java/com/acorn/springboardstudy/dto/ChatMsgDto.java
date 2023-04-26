package com.acorn.springboardstudy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

// JsonIgnore Handler 는 맵퍼에서 lazy 지연로딩할때만 필요해서, 지연로딩 안하는 경우에는
// json 으로 받을때, 없어도 된다.
@Data
public class ChatMsgDto {
    public enum Status {
        ENTER, // 입장
        LEAVE, // 퇴장
        CHAT // 메세지 제출
    }

    @JsonProperty("cmId")
    private int cmId; // pk
    @JsonProperty("crId")
    private int crId; // fk chat_rooms.cr_id
    @JsonProperty("uId")
    private String uId; // fk users.u_id

    private String nickname; // 별칭
    private String content; // 메세지 내용
    private Status status; // 메세지 상태

    // Jackson json 라이브러리가 json 을 응답할때 Date 를 미국 시간대로 설정한다.
    // => JsonFormat 으로 한국시간대로 설정
    @JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("postTime")
    private Date postTime; // 동록 시간

}
