package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import com.acorn.springboardstudy.dto.ReplyLikeDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 순서 실행
class ReplyLikeMapperTest {
    private static ReplyLikeDto replyLikeInsert; // db 의 내용을 바꾸기 위한
    @Autowired
    private ReplyLikeMapper replyLikeMapper;
    @Test
    @Order(1)
    void insertOne(){
        replyLikeInsert=new ReplyLikeDto();
        replyLikeInsert.setBrId(5);
        replyLikeInsert.setUId("user11");
        replyLikeInsert.setStatus("BEST");
        int insertOne = replyLikeMapper.insertOne(replyLikeInsert);
        assertEquals(insertOne,1);

    }
    @Order(2)
    @Test
    void findByUidAndBrId() {
        ReplyLikeDto replyLikeDto = replyLikeMapper.findByUidAndBrId(replyLikeInsert.getUId(), replyLikeInsert.getBrId());
        assertNotNull(replyLikeDto);
    }

    @Test
    @Order(3)
    void countStatusByBrId() {
        LikeStatusCntDto likeStatusCntDto = replyLikeMapper.countStatusByBrId(replyLikeInsert.getBrId());
        assertNotNull(likeStatusCntDto);
    }

    @Test
    @Order(5)
    void countStatusByUId() {
        LikeStatusCntDto likeStatusCntDto = replyLikeMapper.countStatusByUId(replyLikeInsert.getUId());
        assertNotNull(likeStatusCntDto);
    }


    @Test
    @Order(4)
    void updateOne() {
        replyLikeInsert.setStatus("SAD");
        int updateOne=replyLikeMapper.updateOne(replyLikeInsert);
        assertEquals(updateOne,1);
    }

    @Test
    @Order(6) // Order 가 없으면 가장 나중에 실행된다.
    void deleteOne() {
        int deleteOne = replyLikeMapper.deleteOne(replyLikeInsert);
        assertEquals(deleteOne,1);
    }
}