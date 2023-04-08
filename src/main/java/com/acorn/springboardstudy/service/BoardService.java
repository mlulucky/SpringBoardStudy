package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardDto;

import java.util.List;

public interface BoardService {
    // 게시글 서비스 : 리스트, 상세(조회수 올림), 수정, 등록, 삭제
    List<BoardDto> list();
    List<BoardDto> list(String loginUserId);

    BoardDto detail(int bId);
    int register(BoardDto board);
    int modify(BoardDto board);
    int remove(int bId);


}
