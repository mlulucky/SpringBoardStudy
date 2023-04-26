package com.acorn.springboardstudy.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    // 회원가입시 이메일 체크
    public enum StatusType { // 외부에서 사용하면 public - import 되게한다. // 타입은 import 를 해야한다. (get/set 아니라) // UserDto 를 import 할때 따라올수있게
        SIGNUP,EMAIL_CHECK,BLOCK,LEAVE,REPORT
    }
    private StatusType status;
    private String emailCheckCode;

    private String uId;
    private String pw;
    private String name;
    private String phone;
    private String imgPath;
    private String email;
    private java.util.Date postTime;
    private String birth; //유닉스시간을 기준으로 하면 1970 이전에 태어난 사람을 저장할 수 없어서...
    private String gender;
    private String address;
    private String detailAddress;
    private String permission;


    // POJO (get/set) 약속에서 boolean 타입은 get 이 아니라 is 로 사용 => getFollowing (x) -> isFollowing
    private boolean following; // 로그인한 유저가 해당 유저를 팔로잉 중인가? (필드명 isFollowing 쓰면 안된다)
    private List<UserDto> followings; // 팔로우 리스트 users : follows 1:N (from_id=u_id)
    private List<UserDto> followers; // 팔로워 리스트 users : follows 1:N (to_id=u_id)




}
