package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*; // Assertions 패키지 static 으로 등록해서 바로 속성 사용하기

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//🍏MethodOrderer.OrderAnnotation : @Order(0~) () 괄호안 숫자로 순서를 정하겠다. (이외에 함수이름 등으로도 순서를 정할수 있다.)
//🔥** Junit 테스트 개수 보통 2000개 => 빨리 테스트하기 위해서는 동시에 진행하는 것이 제일 좋다(방법이 없으면 순서를 정한다.)
// 순서를 묶어놔서, 전체 테스트로 진행이 가능하다.


class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    // 🔥static 을 쓰는 이유?
    private static UserDto user; // 선언된 값이 없으니까 null // user 를 다른 함수에서도 참조할수 있게끔 전역에서 선언
    // private으로 선언되어 클래스 외부에서 직접 접근할 수 없으며, static 으로 선언되어 클래스 단위로 관리
    @Test
    @Order(1) // 순서 1번째 실행 (첫번째로 실행)
    void insertOne() {
        user=new UserDto(); // 전역에 선언된 user 를 참조
        user.setUId("TestTest478977");
        user.setPw("1234");
        user.setBirth("1986-05-25");
        user.setPhone("test-777-test");
        user.setEmail("TestTest478977@Test.or.com.net");
        user.setName("테스트 유저입니다.");
        user.setAddress("서울시 압구정");
        user.setDetailAddress("압구정역");
        user.setGender("MALE");
        user.setImgPath("/public/imgs/user/TestTest478977.jpeg");
        int insertOne = userMapper.insertOne(user);
        assertEquals(insertOne,1); // Assertions 패키지 static 으로 등록해서 바로 속성 사용하기
    }

    @BeforeAll // 모든 테스트 실행 전에 실행(초기화용 => static 필드로 만들기) // 테스트용 레코드(객체) 만들기
    static void init(){

    }
    @AfterAll // 모든 테스트가 끝나고 실행(정리 => static 필드로 만들기)
    static void destroy(){

    }
    // @Test : 정의된 함수들은 순서 없이 실행되기 때문에 초기화 하거나 정리를 해야한다.
    // 등록 => 수정 => 삭제 (기대하는 순서)
    // 삭제(실패) => 등록(진행중) => 수정(실패) (삭제 먼저 실행시 값이 없으므로 실패)
    //@TestMethodOrder : (Junit 5 부터) test 순서를 지정할 수 있음

    @Test
    @Order(2)
    void findByUId() {
//        UserDto findUser=userMapper.findByUId(user.getUId());
        UserDto findUser=userMapper.findByUId("user01");
        System.out.println("findUser = " + findUser); // 지연로딩 호출 _ 출력
        assertNotNull(findUser);
    }

    @Test
    @Order(3)
    void findByUIdAndPw() {
        UserDto findUser=userMapper.findByUIdAndPw(user);
        assertNotNull(findUser);
    }

    @Test
    @Order(4)
    void findUIdByEmailAndPhoneAndName() {
        String uId= userMapper.findUIdByEmailAndPhoneAndName(user);
        assertNotNull(uId);
    }

    @Test
    @Order(5)
    void updateOne() {
        // 업데이트 테스트 - 유저정보 새로저장
        UserDto user=new UserDto(); // 전역에 선언된 user 를 참조
        user.setUId("TestTest478977");
        user.setPw("12345678");
        user.setBirth("1986-05-20");
        user.setPhone("test-888-test");
        user.setEmail("TestTest478977@Test.or.com");
        user.setName("테스트 유저 수정입니다.");
        user.setAddress("서울시 강남구");
        user.setDetailAddress("강남역");
        user.setGender("NONE");
        user.setImgPath("/public/imgs/user/TestTest478977.png");
        // 새로 저장한 유저정보를 가진 유저를 updateOne 변수에 담음
        int updateOne = userMapper.updateOne(user);

//        user
        UserMapperTest.user=userMapper.findByUId(user.getUId()); // 바꾼 정보를 전역의 유저에 담았다
        System.out.println("UserMapperTest.user = " + UserMapperTest.user);

        // 성공여부 확인
        assertEquals(updateOne,1);
    }

    @Test
    @Order(6)
    void updatePwByUId() { // 비밀번호 확인은 프론트에서만 확인 (서버에서 확인x)
        UserDto user=new UserDto(); // 전역에 선언된 user 를 참조
        user.setUId("TestTest478977");
        user.setPw("비밀번호입니다.");
        int updatePwByUId = userMapper.updatePwByUId(user);
        UserMapperTest.user=userMapper.findByUId(user.getUId());
        System.out.println("UserMapperTest.user = " + UserMapperTest.user);
        assertEquals(updatePwByUId,1);
    }

    @Test
    @Order(7)
    void deleteByUIdAndPw() { // 최종적으로 유저를 지우기 때문에 최종결과로 테이블에서는 유저가 없다.
        int deleteByUIdAndPw = userMapper.deleteByUIdAndPw(user);
        assertEquals(1,deleteByUIdAndPw);
    }


}