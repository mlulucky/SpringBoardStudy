package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    // 채팅방 리스트 🍉
    // 채팅방 등록
    // 채팅방 수정
    // 채팅방 삭제
    List<ChatRoomDto> findAll();


}
