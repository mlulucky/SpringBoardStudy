package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.ChatMsgDto;
import com.acorn.springboardstudy.mapper.ChatMsgMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 스프링 ioc 컨테이너에서 관리받는 객체
@AllArgsConstructor // pojo 형식으로 생성자 생성
public class ChatMsgServiceImp implements ChatMsgService{
   private ChatMsgMapper chatMsgMapper;

    @Override
    public int register(ChatMsgDto chatMsgDto) {
        int register=chatMsgMapper.insertOne(chatMsgDto);
        return register;
    }

    @Override
    public List<ChatMsgDto> list(int crId) {
        List<ChatMsgDto> list=chatMsgMapper.findByCrId(crId);
        return list;
    }

    @Override
    public List<ChatMsgDto> list(int crId, String postTime) {
        List<ChatMsgDto> list=chatMsgMapper.findByCrIdAndGreaterThanPostTime(crId,postTime);
        return list;
    }
}
