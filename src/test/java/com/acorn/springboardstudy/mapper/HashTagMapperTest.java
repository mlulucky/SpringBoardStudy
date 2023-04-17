package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.HashTagDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HashTagMapperTest {
    @Autowired // 의존성 주입
    private HashTagMapper hashTagMapper;

    @Test
    void findByTagContains() {
        List<HashTagDto> tags=hashTagMapper.findByTagContains("홍");
        assertNotNull(tags);
    }

    @Test
    void findByTag() {
        HashTagDto tag = hashTagMapper.findByTag("홍대");
        assertNotNull(tag);
    }

    @Test
    void insertOne() {
        HashTagDto tag=hashTagMapper.findByTag("가로수길맛집");
        if(tag==null){
            int insert=hashTagMapper.insertOne("가로수길맛집");
        }
    }
}