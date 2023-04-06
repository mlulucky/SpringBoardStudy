package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardReplyDto;
import com.acorn.springboardstudy.mapper.BoardReplyMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Spring 이 관리하는
@AllArgsConstructor // BoardReplyMapper 를 받을거니까

public class BoardReplyServiceImp implements BoardReplyService {
    private BoardReplyMapper boardReplyMapper;
    @Override
    public List<BoardReplyDto> list(int bId) {
        List<BoardReplyDto> list=boardReplyMapper.findByBIdAndParentBrIdIsNull(bId);
        return list;
    }

    @Override
    public BoardReplyDto detail(int brId) {
        BoardReplyDto detail=boardReplyMapper.findByBrId(brId);
        return detail;
    }

    @Override
    public int register(BoardReplyDto reply) {
        int register=boardReplyMapper.insertOne(reply); // 중간에 다른 로직이 들어갈수있어서 바로 return x
        return register;
    }

    @Override
    public int modify(BoardReplyDto reply) {
        int modify=boardReplyMapper.updateOne(reply);
        return modify;
    }

    @Override
    public int remove(int brId) {
        int remove=boardReplyMapper.deleteOne(brId);
        return remove;
    }
}
