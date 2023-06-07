package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardLikeDto;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BoardLikeMapperTest {
    @Autowired
    private BoardLikeMapper boardLikeMapper;

    @Test
    void findByBIdAndUId(){
        BoardLikeDto boardLikeDto = boardLikeMapper.findByBIdAndUId(1,"user01");
        System.out.println("boardLikeDto = " + boardLikeDto);
        assertNotNull(boardLikeDto);
    }
    @Test
    void countStatusByBId() {
        LikeStatusCntDto likeStatusCnt = boardLikeMapper.countStatusByBId(10);
        assertNotNull(likeStatusCnt);
    }

    @Test
    void countStatusByUId() {
        LikeStatusCntDto likeStatusCntDto=boardLikeMapper.countStatusByUId("user01");
        assertNotNull(likeStatusCntDto);
    }

    @Test
    void insertUpdateDeleteOne() {
        BoardLikeDto boardLike = new BoardLikeDto();
        // 등록
        boardLike.setBId(10);
        boardLike.setUId("user07"); // 유저 한명은 게시글에 좋아요(상태등록)를 한번만 할 수 있다. (중복할수없게 uk 로 만듬)
        boardLike.setStatus("BEST");
        int insert = boardLikeMapper.insertOne(boardLike);
        // 수정
        boardLike.setStatus("BAD");
        int update=boardLikeMapper.updateOne(boardLike);
        // 삭제
        int delete=boardLikeMapper.deleteOne(boardLike);
        assertEquals(insert+update+delete,3);
    }

}