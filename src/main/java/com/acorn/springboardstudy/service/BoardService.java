package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.BoardImgDto;
import com.acorn.springboardstudy.dto.BoardPageDto;
import com.acorn.springboardstudy.dto.UserDto;

import java.util.List;

public interface BoardService {
    // 게시글 서비스 : 리스트, 상세(조회수 올림), 수정, 등록, 삭제
    List <BoardDto> list(UserDto loginUser, BoardPageDto pageDto);
    // List<BoardDto> list(UserDto loginUser, PageDto pageDto);
    List<BoardDto> tagList(String tag, UserDto loginUser, BoardPageDto pageDto);

    // 이미지 아이디 배열로 받기 (이미지 컴퓨터에서 삭제용_ 이미지 경로 받으려고)
    List<BoardImgDto> imgList(int []biId);

    // 로그인 한 사람이 없을때!
//    List<BoardDto> list(UserDto loginUser);

    BoardDto detail(int bId);
    int register(BoardDto board, List<String> tags);
    int modify(BoardDto board, int[] delImgIds, List<String> tags, List<String> delTags);
    int remove(int bId);


}
