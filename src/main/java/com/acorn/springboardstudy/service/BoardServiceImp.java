package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // @Component 의 일부
@AllArgsConstructor // 의존성 주입 + 생성자 생성
public class BoardServiceImp implements BoardService {
    private BoardMapper boardMapper; // @AllArgsConstructor

    @Override
    public List<BoardDto> list() {
        List<BoardDto> list=boardMapper.findAll();
        return list;
    }

    @Override
    @Transactional // AOP(관점지향언어)의 일종(관심사 분리). 커밋과 롤백을 따로 // 시작지점으로 커밋을 하고 예외발생시 커밋지점으로 다시 롤백
    public BoardDto detail(int bId) {
        // 시작지점(dataSource.getConnection().commit() )을 커밋으로 하고 그 이후를 자동으로 try,catch

        // 예외처리 부분
        boardMapper.updateIncrementViewCountBId(bId); // 상세글보기 시 조회수 올리기
        BoardDto detail=boardMapper.findByBId(bId);

        // 예외 발생시 - 커밋지점으로 롤백 : dataSource.getConnection().rollBack()
        return detail;
    }

    @Override
    public int register(BoardDto board) {
        int register=boardMapper.insertOne(board);
        return register;
    }

    @Override
    public int modify(BoardDto board) {
        int modify=boardMapper.updateOne(board);
        return modify;
    }

    @Override
    public int remove(int bId) {
        int remove=boardMapper.deleteOne(bId);
        return remove;
    }
}
