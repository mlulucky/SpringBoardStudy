package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 로그인(등급조회 후 로그인 가능), 개인정보조회(등급조회), 수정, 삭제(아이디 비밀번호 확인), 회원가입
    // 아이디찾기(email,phone,이름 입력시 아이디 반환)
    // 비밀번호찾기(실제로는 변경) => [email or 핸드폰 인증] 로 변경페이지를 반환(pwUpdate.do) => 비밀번호 변경Action

    UserDto findByUId(String uId);
    UserDto findByUIdAndPw(UserDto user); // 로그인 // permission 이 private 이 아닌 사람만 로그인
    String findUIdByEmailAndPhoneAndName(UserDto user); // 아이디 찾기 // 이메일, 핸드폰번호, 이름 을 매개변수로 받는데, 3개이상을 매개변수로 받는 경우 타입으로 받는게 좋다.
    int updateOne(UserDto user);
    int updatePwByUId(UserDto user); // 비밀번호 변경
    int insertOne(UserDto user);
    int deleteByUIdAndPw(UserDto user);

    int setLoginUserId(String uId); // mysql 에서 사용한 변수 등록
    int setLoginUserIdNull();
}
