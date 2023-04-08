package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardLikeDto;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import com.acorn.springboardstudy.mapper.BoardLikeMapper;
import com.acorn.springboardstudy.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardLikeServiceImp implements BoardLikeService {
    private BoardLikeMapper boardLikeMapper;
    private UserMapper userMapper;

    // *좋아요 수정, 등록, 삭제
    @Override
    public BoardLikeDto detail(int bId, String uId) {
        BoardLikeDto detail=boardLikeMapper.findByBIdAndUId(bId,uId);
        return detail;
    }


    @Override
    public LikeStatusCntDto read(int bId) {
        LikeStatusCntDto read=boardLikeMapper.countStatusByBId(bId);
        return read;
    }

    @Override
    public LikeStatusCntDto read(int bId, String loginUserId) {
        userMapper.setLoginUserId(loginUserId); // 변수로 저장
        LikeStatusCntDto read=boardLikeMapper.countStatusByBId(bId); // 로딩
        userMapper.setLoginUserIdNull(); // 변수 삭제
        return read;
    }

    @Override
    public int register(BoardLikeDto like) {
        int register=boardLikeMapper.insertOne(like);
        return register;
    }

    @Override
    public int modify(BoardLikeDto like) {
        int modify=boardLikeMapper.updateOne(like);
        return modify;
    }

    @Override
    public int remove(BoardLikeDto like) {
        int remove=boardLikeMapper.deleteOne(like);
        return remove;
    }
}
