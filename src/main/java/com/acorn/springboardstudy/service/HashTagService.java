package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardHashTagDto;
import com.acorn.springboardstudy.dto.HashTagDto;

import java.util.List;

public interface HashTagService {
    // 해시태그 서비스
    // 검색
    // 게시글에 해시태그 등록 (만약 해시태그가 없다면 해시태그도 등록)
    // => 게시글 1번에 #처음등록하는해시태그 => 해시태그 등록 + 게시글에 해시태그 등록
    // 게시글을 수정할때 해시태그 삭제
    List<HashTagDto> search(String tag);

    // 보드 해시태그
    // 보드해시태그 서비스를 따로 만들지 않고 여기서 진행 -> 해시태그만 따로 등록하는 경우가 없고 보드 등록할때 해시태그도 등록해서
    // 해시태그 서비스 따로 안만듬
    int register(BoardHashTagDto boardHashTagDto);

    int remove(BoardHashTagDto boardHashTagDto);

    // 댓글 해시태그 _ 이름이 같아도 매개변수이름,타입이 같으면 상관없다 => 오버로드
//    int register(ReplyHashTagDto replyHashTagDto);



}
