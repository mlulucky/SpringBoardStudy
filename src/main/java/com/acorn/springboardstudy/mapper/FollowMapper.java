package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.FollowDto;
import com.acorn.springboardstudy.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    // 로그인한 유저가 팔로잉(구독)하는 유저들(팔로우)
    // 로그인한 유저를 팔로잉(구독)하는 유저들(팔로워)
    // (나를 구독하는 사람을 취소)팔로워를 삭제
    // (내가 구독하는 사람을 취소)팔로우를 삭제
    // 팔로우를 등록
    List<UserDto> findByFromId(String uId); // 팔로우
    List<UserDto> findByToId(String uId); // 팔로워
    int deleteByFromIdAndToId(FollowDto followDto); // 팔로워,팔로우 삭제 (from_id, to_id 사용)
    int insertOne(FollowDto followDto);

    boolean findByToIdAndFromIdIsLoginUserId(String uId);





}
