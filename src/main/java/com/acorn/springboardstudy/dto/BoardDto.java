package com.acorn.springboardstudy.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data // lombok - getter/setter (POJO) 자동생성
public class BoardDto {
    private int bId; // pk
    private String uId; // fk user.u_id 참조
    private Date postTime; // default(등록 기본값) CURRENT_TIMESTAMPE (기본값으로 현재시간 등록)
    private Date updateTime; // default on update CURRENT_TIMESTAMP
    private String status; // enum [PUBLIC, PRIVATE, REPORT, BLOCK]
    private String title;
    private String content;
    private int viewCount;

    // 🍎BoardDto 와 조인하는 DTO 선언 - 댓글, 유저, 이미지
    private LikeStatusCntDto likes; // 1:N = boards : board_likes 이지만 집계한 결과만 조회
    private List<BoardReplyDto> replies; // 1:N = boards : board_replies
    private UserDto user; // N : 1 = boards : users (보드는 유저 1명. 유저1명은 여러 게시글 작성)
    private List<BoardImgDto> imgs; // 1:N 조인 = boards : board_imgs
}
