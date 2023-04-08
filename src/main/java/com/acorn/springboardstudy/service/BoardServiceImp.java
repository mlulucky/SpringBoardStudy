package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.mapper.BoardMapper;
import com.acorn.springboardstudy.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // @Component 의 일부
@AllArgsConstructor // 의존성 주입 + 생성자 생성
public class BoardServiceImp implements BoardService {
    private BoardMapper boardMapper; // @AllArgsConstructor
    private UserMapper userMapper;

    // 로그인 한 사람이 없을때!
    @Override
    public List<BoardDto> list() {
        List<BoardDto> list=boardMapper.findAll();
        return list;
    }

    // 로그인 한 사람이 있을때~!
    @Override
    public List<BoardDto> list(String loginUserId) {
//        List<BoardDto> list=boardMapper.findAll(loginUserId); // 서브쿼리로 좋아요 불러오기
        userMapper.setLoginUserId(loginUserId); // 로그인한 유저 아이디를 mysql 서버에 변수로 등록
        List<BoardDto> list=this.boardMapper.findAll(); // 지연로딩으로 좋아요 불러오기
        userMapper.setLoginUserIdNull(); // 사용이 끝나서 삭제 // 지연로딩으로 조인이 조금 늦어서 불러오기전에 먼저 삭제를 해서(null 을 만듬) 상태가 자꾸 null 이 됨.
        // 보드에서 조회할때는 즉시로딩으로 바꾸기
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
