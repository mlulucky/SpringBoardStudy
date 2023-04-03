package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// @Component // spring container(IoC(Inversion of Controller) == Ioc 컨테이너(== 스프링컨테이너)) 가 관리하는 Bean 객체
// IoC(제어의 역전) : 객체를 내부에서 생성하는 것이 정상적인 제어 이고 관심사를 분리해서 객체를 주입하는 형태(관점지향 언어)로 변경하는 것이 제어의 역전
// IoC 를 이용해서 관심사를 분리할 수 있고 이를 통해 관점지향언어(AOP)를 구현
//
// 🍏DI(의존성 주입) : private 필드인 @Autowired or (생성자로 주입((POJO - private 을 생성자에 주입하는 것도 POJO)  or setter 함수) 정의하고 호출 => 주입
// => 테스트의 경우 생성자를 정의할수없어서 @Autowired 로 객체를 주입받는다.
// 🍏DIP(의존성 주입 원칙) : 주입받는 객체의 타입은 꼭 🍒인터페이스(구현이 덜된,객체를 여러형태로 구현가능,유연성,확장성)로 정의하기(모듈을 유연하게 확장하기 위해)

// 우리 눈에 보이진 않지만 스프링이 컨테이너를 관리하고 있다.
// 🍎객체가 필요할때마다 객체를 생성하여 사용하는 것은 객체에 너무 🔥의존적이다.
// => 객체를 빼면 안되서 코드가 복잡해지고, 객체가 1000개가 필요하면 객체를 1000번 생성해야한다.
// => 객체의 타입이 바뀌면 ex) 객체 이름이 바뀌면 - 리팩터링이 없으면 객체이름을 다시 새로 써야한다.
// => 객체의 생성자 규칙이 바뀌면 ex) 생성자의 매개변수가 바뀌면 - 1000번을 전부 수정해야한다.

// 객체 생성을 IoC 컨테이너(== 스프링 컨테이너)에 위임하기
// 🍏IoC 컨테이너에서 객체를 관리(생성+관리) -> 모듈에 객체를주입(제어의역전) 시킨다. => 모듈을 객체와 분리시킨다.


@Service // @Component 하위 자식 어노테이션으로 service 를 관리하는(관심사 분리) 명시적 의미와 @Transactional 을 정의 가능
public interface UserService {
    // 로그인 유저상세 수정 회원가입 삭제
    UserDto login(UserDto user);
    UserDto detail(String uId);
    int modify(UserDto user);
    int signup(UserDto user);
    int dropout(UserDto user);

}
