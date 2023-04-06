package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {
    // @Component 로 정의된 클래스만 DI(의존성 주입) 할 수 있다.

    // ---- POJO 로 DI 정의
    private UserMapper userMapper; // 인터페이스
    // DIP 에 의해서 인터페이스로 정의 (DIP 원칙으로 인터페이스를 주입)
    // => **인터페이스로 모듈을 유연하게 작성했기 때문에 Mybatis 가 객체로 구현할 수 있다.(DIP 덕분에 Mybatis 사용)
    public UserServiceImp(UserMapper userMapper){
        this.userMapper=userMapper;
    }
    // ----- end


    @Override
    public UserDto login(UserDto user) {
        return userMapper.findByUIdAndPw(user);
    }

    @Override
    public UserDto detail(String uId) {
        return userMapper.findByUId(uId);
    }

    @Override
    public int modify(UserDto user) {
        return userMapper.updateOne(user);
    }

    @Override
    public int signup(UserDto user) {
        return userMapper.insertOne(user);
    }

    // @Transactional // 🔥선언 -> 함수안에 있는 모든것은 하나의 트랜잭션 - 롤백가능하다.
    @Override
    public int dropout(UserDto user) {
        return userMapper.deleteByUIdAndPw(user);
    }
}
