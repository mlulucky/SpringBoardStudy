package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardReplyDto;

import java.util.List;

public interface BoardReplyService {
    // 등록, 수정, 삭제, 디테일, 리스트(게시글이 있는 == 게시글 번호가 있는)
    List<BoardReplyDto> list(int bId); // 댓글은 꼭 게시글에 있으니까 // 게시글이 있는 == 게시글 번호가 있는
    BoardReplyDto detail(int brId);
    int register(BoardReplyDto reply);
    int modify(BoardReplyDto reply);
    int remove(int brId);




}
