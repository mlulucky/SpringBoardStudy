package com.acorn.springboardstudy.mapper;
import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.BoardPageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Junit 단위 테스트는 spring 과 별개로 실행되기 때문에 객체 주입을 받을 수 없는데
// 🍏Spring boot 3.0 부터 @SpringBootTest 를 테스트에 정의하면 spring 에서 생성하는 객체를 주입받을 수 있다.
class BoardMapperTest {
    // Junit 은 클래스의 생성자를 정의할 수 없다. 그래서 생성자 없이 객체를 주입받는 @Autowired 를 작성해야 한다.
    // 의존성 주입
    @Autowired
    private BoardMapper boardMapper; // 객체 // 싱글톤 패턴
    // == private BoardMapper boardMapper=null;
    // 객체를 new 생성하지 않았는데도, boardMapper 객체를 사용할 수 있다. (왜? Autowired 의존성 주입으로)

    // 챗지피티 - @Mapper 어노테이션 설명
    // @Mapper 어노테이션은 인터페이스에 적용되며, 해당 인터페이스를 구현하는 클래스를 동적으로 생성합니다. 이 클래스는 MyBatis에서 자동으로 인스턴스화되어 SQL 쿼리를 실행하는 데 사용됩니다.
    // 따라서 @Mapper 어노테이션을 사용하여 인터페이스를 정의하면, MyBatis는 해당 인터페이스를 구현하는 클래스를 생성하고, 이 클래스는 SqlSessionFactory에서 생성된 SQL 세션을 사용하여 데이터베이스와 상호 작용합니다.
    // 즉, @Mapper 어노테이션은 SQL 세션 팩토리를 직접적으로 의존하지 않지만, 인터페이스를 구현하는 클래스가 SqlSessionFactory를 사용하여 SQL 세션을 가져와서 데이터베이스와 상호 작용합니다.

    @Test
    void page(){
        int totalRows=13;
        int offset=5;
        double result=((double)totalRows)/offset;
        System.out.println("result = " + result); // 2.6
        System.out.println("(Math.ceil(totalRows/offset)) = " + (Math.ceil(((double)totalRows/offset)))); // 3
        // Math.ceil : 실수 올림
        // int 정수/정수 => 정수 (정수끼리의 연산은 정수가 나온다)
        // double 실수/실수 => 실수
        // 실수 / 정수 => 실수
        // 실수결과가 나오려면 둘중하나는 실수가 나와야 한다.
    }
    
    @Test
    void findAll() {
//        BoardPageDto pageDto=new BoardPageDto();
//        List<BoardDto> boardList=boardMapper.findAll(pageDto);
//        System.out.println("boardList = " + boardList);
//        assertNotNull(boardList); // null 이 아니면 성공! (기본값은 오류발생시 try/catch)

    }

    @Test
    void findByBId() {
        BoardDto board= boardMapper.findByBId(4); // 조회하고 싶은 번호 입력
//        System.out.println("board = " + board); // 호출(출력,조회)
        // 댓글 - 지연로딩 (fetch=lazy) : 호출(출력,조회)할 때(트리거(get,toString)) 조회
        // fetch=eager : 즉시로딩 - 출력을 안해도 즉시 조회된다. => 시퀄라이즈에서 배운내용.
        // => 호출, 출력이 없으면 댓글이 조회되지 않는다.
        System.out.println("board.getReplies() = " + board.getReplies());
        System.out.println("board.getUser() = " + board.getUser());
        System.out.println("board.getImgs() = " + board.getImgs());
        System.out.println("board.getLikes() = " + board.getLikes());
        assertNotNull(board); // null 이 아니면 성공! (기본값은 오류발생시 try/catch)
        //🔥association 은 조회만 하는것! 정렬을 할려면 조인을 해야한다.
        // 좋아요순으로 정렬을 하려면 조인을 해야한다.


        //🍏결과
        // DriverSpy 라이브러리 종속성주입으로 테스트 실행만으로 로그 결과가 나온다 -> 4번게시글 보드 로그 확인가능
        // 보드의 댓글 출력(조회) - board.getReplies() =  + board.getReplies()
        // 보드의 유저 출력(조회) - board.getUser() =  + board.getUser()

        // 보드를 출력한건데, 보드의 댓글과 유저가 출력가능한 이유는?
        // BoardMapper.xml 에서 resultMap 안에 association - 유저 , collection - 댓글 코드를 추가하였기 때문에
        // resultMap 으로 조회(<select>)할 때 <collection select 코드가 있으면> 해당 필드()를 다른 내역을 조회해서 파싱
        //         column="b_id : 조회할때 사용할 파라미터의 값

//        <association property="user"
//        select="com.acorn.springmavenboard.mapper.UserMapper.findByUId"
//        column="u_id"
//        fetchType="lazy"/> <!-- 글쓴이 유저가 누군지 확인 --> <!-- 컬렉션이 가장 아래에 위치 -->
//        <collection property="replies"
//        select="com.acorn.springmavenboard.mapper.BoardReplyMapper.findByBIdAndParentBrIdIsNull"
//        column="b_id"
//        fetchType="lazy"/>

    }
//    mybatis 쿼리를 실행할때마다 쿼리의 내역을 보는 라이브러리 종속성 추가
//    log4jdbc-log4j2-jdbc4.1 라이브러리
//    (접속해서 mybatis 가 쿼리를 실행하는걸 스파이처럼 보고있다가 내역을 보내준다)
//    테스트 실행시 로그 sql 출력 : Connection.prepareStatement(SELECT * FROM boards WHERE b_id=?) returned net.sf.log4jdbc.sql.jdbcapi.PreparedStatementSpy@4c50cd7d
//  |-----|-------|----------------------|----------------------|-------|-------------|--------|-----------|
//  |b_id |u_id   |post_time             |update_time           |status |title        |content |view_count |
//  |-----|-------|----------------------|----------------------|-------|-------------|--------|-----------|
//  |9    |user09 |2023-03-13 11:06:28.0 |2023-03-28 09:35:50.0 |REPORT |보드 수정 테스트 안녕 |내용입니다.  |0    |
//  |-----|-------|----------------------|----------------------|-------|-------------|--------|-----------|

    @Test
    void insertOne() {
        BoardDto board=new BoardDto();
        board.setTitle("보드 등록 테스트 안녕");
        board.setContent("내용입니다.");
        board.setUId("admin");
        int insert=boardMapper.insertOne(board);
        System.out.println("insert = " + insert);
        System.out.println("board = " + board); // 보드 번호를 넣은적이 없는 보드 번호가 생긴다.
        // BoardMapper 에서 설정해서 => useGeneratedKeys="true" keyProperty="bId"
        assertEquals(insert,1); // 등록성공시 1 결과면 테스트 통과 (기본값은 오류발생시 try/catch)

    }
    
    @Test
    void insertOneAndDeleteOne() { // 만들자 마자 바로 삭제하는 테스트
        BoardDto board=new BoardDto();
        board.setTitle("보드 등록 테스트 안녕");
        board.setContent("내용입니다.");
        board.setUId("admin");
        int insert=boardMapper.insertOne(board);
        System.out.println("insert = " + insert);
        System.out.println("board = " + board); // 보드 번호를 넣은적이 없는 보드 번호가 생긴다.
        // BoardMapper 에서 설정해서 => useGeneratedKeys="true" keyProperty="bId"
        
        int delete=boardMapper.deleteOne(board.getBId());
        System.out.println("delete = " + delete);
        assertEquals(insert+delete,2); // 등록/삭제 성공시 성공 결과 2개 이면 테스트 성공 (기본값은 오류발생시 try/catch)
    }

    @Test
    void updateOne() {
        BoardDto board=new BoardDto();
        board.setTitle("보드 수정 테스트 안녕");
        board.setContent("내용입니다.");
        board.setBId(9);
        int update=boardMapper.updateOne(board);
        assertEquals(update,1); // 수정성공 (결과 1이면 성공)
        BoardDto updateBoard=boardMapper.findByBId(9);
        System.out.println("updateBoard = " + updateBoard);
        assertEquals(update,1); // 업데이트 성공시 결과 1이면 테스트 성공 (기본값은 오류발생시 try/catch)
        
    }

    @Test
    void updateStatusByBId() {
        BoardDto board=new BoardDto();
        board.setBId(9);
        board.setStatus("REPORT");
        int update=boardMapper.updateStatusByBId(board);

        BoardDto updateBoard=boardMapper.findByBId(9);
        System.out.println("updateBoard = " + updateBoard);
        assertEquals(update,1); // 업데이트 성공시 결과 1이면 테스트 성공 (기본값은 오류발생시 try/catch)
    }

    @Test
    void updateIncrementViewCountBId() { // 조회수 증가
        int updateIncrementViewCountBId = boardMapper.updateIncrementViewCountBId(1);
        assertEquals(updateIncrementViewCountBId,1);
    }

    @Test
    void findByTag() {
        List<BoardDto> boards = boardMapper.findByTag("홍대");
        assertNotNull(boards);
    }

//    @Test
//    void updateStatusByBId() {
//        int update=boardMapper.updateStatusByBId("PUBLIC",9);
//        BoardDto updateBoard=boardMapper.findByBId(9);
//        System.out.println("updateBoard = " + updateBoard);
//        Assertions.assertEquals(update,1); // 업데이트 성공시 결과 1이면 테스트 성공 (기본값은 오류발생시 try/catch)
//    }
}