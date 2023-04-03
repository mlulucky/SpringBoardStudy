package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardImgDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardImgMapperTest {
    @Autowired
    private BoardImgMapper boardImgMapper;

    @Test
    void findByBId() {
        List<BoardImgDto> imgs=boardImgMapper.findByBId(2);
        Assertions.assertNotNull(imgs);
    }

    @Test
    void insertOneAndDeleteOne() { // Mapper.xml 의 sql 쿼리에 keyProperty, GenerateKey(자동생성 pk) 속성 추가하기!
        BoardImgDto boardImgDto=new BoardImgDto();
        boardImgDto.setBId(2);
        boardImgDto.setImgPath("테스트용 이미지");
        int insert= boardImgMapper.insertOne(boardImgDto); // biId 번호 자동생성
        int delete=boardImgMapper.deleteOne(boardImgDto.getBiId()); // 생성된 biId 번호 삭제 // BoardImgDto 의 필드 BiId 값 불러오기
        Assertions.assertEquals(insert+delete,2); // 등록+삭제 실행결과 2
    }

    @Test
    void deleteOne() {
    }
}