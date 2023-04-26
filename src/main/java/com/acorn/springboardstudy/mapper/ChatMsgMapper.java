package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.ChatMsgDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMsgMapper {
    // 구독(subcribe) 중인 룸에서 메세지 보낸다 (데이터베이스에 추가하는 것 insert)
    // 룸에서 메세지 리스트를 받아야 한다. (select)
    // 가장 최근에 받은 메세지의 다음 메세지 리스트를 조회
    // 삭제x,수정x

    int insertOne(ChatMsgDto chatMsgDto);

    List<ChatMsgDto> findByCrId(int crId); // 1번방 의 메세지 찾기

    List<ChatMsgDto> findByCrIdAndGreaterThanPostTime(@Param("crId") int crId, @Param("postTime") String postTime);

}
