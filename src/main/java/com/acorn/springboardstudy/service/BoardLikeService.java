package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardLikeDto;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;

public interface BoardLikeService {
    // 해당 게시글의 좋아요 내역의 집계한 결과(LikeStatusCntDto) 조회
    // 좋아요를 등록, 수정, 삭제(취소)
    // 로그인한 유저가 좋아요를 했는지 확인!
    LikeStatusCntDto read(int bId); // 로그인 안한 사람 조회 // 해당 게시글의 번호가 꼭 필요하다
    LikeStatusCntDto read(int bId, String loginUserId); // 로그인 한 사람이 조회하는 경우
    BoardLikeDto detail(int bId, String uId); // 로그인한 유저가 좋아요를 했는지 확인!
    int register(BoardLikeDto like);
    int modify(BoardLikeDto like);
    int remove(BoardLikeDto like); // 글쓴이랑 bId 둘다 받아서, 그냥 Dto 전체로 받아버림.


}
