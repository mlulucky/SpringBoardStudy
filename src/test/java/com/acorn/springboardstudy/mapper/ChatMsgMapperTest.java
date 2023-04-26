package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.ChatMsgDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatMsgMapperTest {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Test
    void insertOne() {
        ChatMsgDto chatMsgDto=new ChatMsgDto();
        chatMsgDto.setCrId(1);
        chatMsgDto.setNickname("은정");
        chatMsgDto.setUId("user01");
        chatMsgDto.setContent("입장");
        chatMsgDto.setStatus(ChatMsgDto.Status.ENTER);
        chatMsgMapper.insertOne(chatMsgDto);

    }

    @Test
    void findByCrId() {
        chatMsgMapper.findByCrId(1);
    }

    @Test
    void findByCrIdAndGreaterThanPostTime() {
        chatMsgMapper.findByCrIdAndGreaterThanPostTime(1,"2023-04-25 15:25:36");
    }
}