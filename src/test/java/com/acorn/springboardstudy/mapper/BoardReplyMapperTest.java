package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardReplyDto;
import static org.junit.jupiter.api.Assertions.*;
// static 을 추가하면 Assertions 안에 있는 모든것들을 Assertions 를 사용하지 않고 바로 쓸 수 있다.
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest // Junit 테스트할때 SpringbootTest 실행하면서 의존성 주입
class BoardReplyMapperTest {
    // 드라이버 스파이가 예전것을 쓰고 있어서 오류문구 발생한다 => Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
    @Autowired // 의존성 주입 // 싱글톤 패턴과 같이 private 을 같이 쓴다
    private BoardReplyMapper boardReplyMapper;


    @BeforeAll // 테스트의 가장먼저 시작
    static void init(){ // 댓글 등록을 먼저 해놓고 나머지 수정,삭제를 하는게 좋다.

    }

    @Test
    void findByBIdAndParentBrIdIsNull() {
        List<BoardReplyDto> boardReplies=boardReplyMapper.findByBIdAndParentBrIdIsNull(4);
        System.out.println("boardReplies = " + boardReplies);
        assertNotNull(boardReplies);
//        Assertions.assertNotNull(boardReplies);
    }

    @Test
    void findByParentBrId() {
        List<BoardReplyDto> boardReplies = boardReplyMapper.findByParentBrId(16);
        System.out.println("boardReplies = " + boardReplies);
        assertNotNull(boardReplies);
    }

    @Test
    void findByBrId() {
        BoardReplyDto boardReply = boardReplyMapper.findByBrId(16);
        System.out.println("boardReply = " + boardReply);
        assertNotNull(boardReply);
    }

    // 단위 테스트는 하나하나를 테스트하는 것으로 한꺼번에 하는건 별로 좋지는 않다.
    // => @BeforeAll 로 테스트의 가장먼저 실행으로 등록을 해놓고
    // @Test 로 수정, 삭제를 테스트하는게 좋다.
    @Test
    void insertUpdateDeleteOne() { // 등록+업데이트+삭제 테스트
        BoardReplyDto boardReplyDto=new BoardReplyDto();
        boardReplyDto.setBId(6); // 6번 게시글의
        boardReplyDto.setParentBrId(15); // 16번 댓글이 부모댓글 // 대댓글
//        boardReplyDto.setParentBrId(16); // 16번 댓글이 부모댓글 // 대댓글
        boardReplyDto.setUId("user08"); // 댓글 작성한 유저아이디
        boardReplyDto.setImgPath("테스트 대대댓글이미지"); // 댓글 이미지
        boardReplyDto.setContent("user08이 게시글 15의 대댓글에 작성한 대대댓글 !!!"); // 댓글 내용
        int insertOne = boardReplyMapper.insertOne(boardReplyDto);
        System.out.println("boardReplyDto = " + boardReplyDto);

//        boardReplyDto.setImgPath("수정대댓글이미지~");
//        boardReplyDto.setContent("내용을 수정하는 테스트 진행중");
//        int updateOne = boardReplyMapper.updateOne(boardReplyDto);

//        int deleteOne = boardReplyMapper.deleteOne(boardReplyDto.getBId());
        assertEquals(insertOne,1);
//        Assertions.assertEquals(insertOne+updateOne,2);
//        Assertions.assertEquals(insertOne+updateOne+deleteOne,3);
    }




}