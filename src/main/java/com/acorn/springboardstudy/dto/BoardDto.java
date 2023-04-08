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
    // private BoardLikeDto loginUserLike; // 로그인한 유저가 좋아요 한 내역 (좋아요,싫어요,나빠요,슬퍼요) // board_like uk 유니크키(b_id,u_id) : 유니크키) 하나만 있거나, 없거나
    private String loginUserLikeStatus; // 로그인을 한 유저가 좋아요를 한 status 만 받아오면 됨. (전체내역 BoardLikeDto 을 안받아와도 된다 => String )

    // 🍎BoardDto 와 조인하는 DTO 선언 - 댓글, 유저, 이미지역
    private LikeStatusCntDto likes; // 숫자 // 1:N = boards : board_likes 이지만 집계한 결과만 조회
    private List<BoardReplyDto> replies; // 댓글 // 1:N = boards : board_replies
    private UserDto user; // 김철철씨 // N : 1 = boards : users (보드는 유저 1명. 유저1명은 여러 게시글 작성)
    private List<BoardImgDto> imgs; // 이미지 // 1:N 조인 = boards : board_imgs
}
