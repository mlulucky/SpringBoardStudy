package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.ChatRoomDto;

import java.util.List;

public interface ChatRoomService {
    // 룸 리스트
    // 룸 등록
    // 룸 수정
    // 룸 삭제
    List<ChatRoomDto> list();
    int register(ChatRoomDto chatRoomDto);
    int modify(ChatRoomDto chatRoomDto);
    int remove(int crId);

}
