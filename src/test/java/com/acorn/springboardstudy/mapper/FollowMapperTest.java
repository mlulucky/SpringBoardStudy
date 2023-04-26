package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.FollowDto;
import com.acorn.springboardstudy.dto.UserDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FollowMapperTest {
    @Autowired
    private FollowMapper followMapper;
    @Order(1)
    @Test
    void insertOne() {
        FollowDto followDto=new FollowDto();
        followDto.setFromId("user01");
        followDto.setToId("user17"); // user01 이 user20 을 팔로잉(구독)한다. => 중복 팔로잉은 안됀다 => UNIQUE 키로 만들어서
        int insert= followMapper.insertOne(followDto);
        assertEquals(insert,1);
    }
    @Order(2)
    @Test
    void findByFromId() {
        List<UserDto> followings=followMapper.findByFromId("user01"); // user01 이 팔로잉하는 사람들
        assertNotNull(followings);
    }
    @Order(3)
    @Test
    void findByToId() {
        List<UserDto> followers=followMapper.findByToId("user01"); // user01 를 팔로워(구독)하는 사람들
    }

    @Order(4)
    @Test
    void deleteByFromIdAndToId() {
        FollowDto followDto=new FollowDto();
        followDto.setFromId("user01");
        followDto.setToId("user17");
        int delete= followMapper.deleteByFromIdAndToId(followDto);
        assertEquals(delete,1);
    }




}