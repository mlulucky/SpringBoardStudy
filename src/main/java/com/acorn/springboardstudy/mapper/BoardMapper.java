package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardDto;
import com.github.pagehelper.Page;
import com.acorn.springboardstudy.dto.PageDto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 챗지피티 - @Mapper 어노테이션 설명
// @Mapper 어노테이션은 인터페이스에 적용되며, 해당 인터페이스를 구현하는 클래스를 생성
// 1. 이 클래스는 MyBatis 에서 자동으로 인스턴스(객체)화
// => ex) MapperTest 파일에서 인터페이스 테스트시 @Autowired 로 BoardMapper 인터페이스를 의존성 주입하면 바로 객체로 쓸수 있다.
// 2. 이 클래스는
// Mapper.xml 을 기반으로 SqlSessionFactory 에서 생성된 SQL(세션,쿼리) 을 사용하여 데이터베이스와 상호 작용합니다.


@Mapper // Mybatis 의 session factory(컨테이너) 에서 생성 및 관리하는 컴포넌트. 컨포넌트(컨테이너에서 관리되는 객체)로 생성할 타입 이다 라고 선언?
@Repository // 🔥JDBC dao 를 만들어서 Spring Container 로 관리하는 스프링 컴포넌트 // 컨포넌트 , spring 컨테이너 의 dao 의 의미
public interface BoardMapper {
    // 리스트, 상세, 등록, 수정, 삭제, 신고
    // 상세 보기시 조회수 올리기
    // 인터페이스는 모두 public (생략가능)

    // 🍏함수를 호출. 실행하면 Mapper.xml 의 sql 쿼리 문이 실행되고
    // resultMap(resultType)맵핑된 결과가 나온다.
//    Page<BoardDto> findAll(); // com.github.pagehelper.Page > List+page 결과를 가지고 있다.
//    List<BoardDto> findByTag(String tag); // 다이나믹 쿼리 실행
//    List<BoardDto> findAll(String loginUserId); // 다이나믹 쿼리 실행

    List<BoardDto> findAll(PageDto pageDto);
    int countAll(PageDto pageDto); // 페이지 네비게이션의 수를 알기 위해
    // List<BoardDto> findAll(String loginUserId); // 다이나믹 쿼리 실행
    List<BoardDto> findByTag(String tag); // 해시태그로 게시물 검색

    BoardDto findByBId(int bId);
    int insertOne(BoardDto board);
    int updateOne(BoardDto board);
    int deleteOne(int bId);

//    int updateStatusByBId(String status, int bId);

    int updateStatusByBId(BoardDto board);
//    int updateStatusByBId(@Param(value="status") String status, @Param(value="bId") int bId);
    // status - mybatis 예약어라서 status 인식 안될수있다.

    int updateIncrementViewCountBId(int bId); // 게시글 조회수 증가
}
