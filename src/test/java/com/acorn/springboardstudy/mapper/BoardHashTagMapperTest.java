package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardHashTagDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 순서를 매기면 전체 테스트를 실행해야 한다.
class BoardHashTagMapperTest {

    @Autowired
    private BoardHashTagMapper boardHashTagMapper;
    static BoardHashTagDto boardHashTagDto;

    @Test
    @Order(1) // @TestMethodOrder 으로 순서를 매긴다
    void insertOne() {
        boardHashTagDto=new BoardHashTagDto();
        boardHashTagDto.setTag("에이콘아카데미"); // 게시글에 해시태그 등록! (해시태그 목록에 저장된 해시태그 등록가능)
        boardHashTagDto.setBId(1); // 1번 게시글에 해시태그 등록하기
        int insert= boardHashTagMapper.insertOne(boardHashTagDto);
        assertTrue(insert>0);
    }


    @Test
    @Order(2)
    void findByBId() {
        List<BoardHashTagDto> tags = boardHashTagMapper.findByBId(1);
        assertNotNull(tags);
//        |------|-----|-------|
//        |bh_id |b_id |tag    |
//        |------|-----|-------|
//        |11    |1    |food   |
//        |12    |1    |travel |
//        |13    |1    |먹심     |
//        |10    |1    |한국     |
//        |1     |1    |홍대     |
//        |9     |1    |홍대맛집   |
//        |------|-----|-------|
    }

    @Test
    @Order(3)
    void countByTag() {
        int cnt= boardHashTagMapper.countByTag("홍대");
        assertTrue(cnt>0);
    }

    @Test
    @Order(4)
    void deleteOne() {
        int del= boardHashTagMapper.deleteOne(boardHashTagDto);
        assertTrue(del>0);
    }
}