package com.acorn.springboardstudy;

import com.acorn.springboardstudy.interceptor.AutoLoginInterceptor;
import com.acorn.springboardstudy.interceptor.LoginCheckInterceptor;
import com.acorn.springboardstudy.interceptor.MsgRemoveInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // @Component 스프링 실행시 설정 파일
// WebMvcConfigurer : 요청과 응답과 관련된 설정
@AllArgsConstructor // 의존성 주입 + 생성자에 추가된다
public class IntercepterConfig implements WebMvcConfigurer {
    // implements : 인터페이스를 클래스에서 구현
    // default 메서드인경우 구현을 강제하지 않아도 된다.

    private AutoLoginInterceptor autoLoginInterceptor;
    private LoginCheckInterceptor loginCheckInterceptor; // @AllArgsConstructor // 의존성 주입 + 생성자에 추가된다
    private MsgRemoveInterceptor msgRemoveInterceptor;

    @Override
    // addInterceptors : 인터셉터(== 미들웨어) 설정
    // 미들웨어(로그인체크) / 컨트롤러 분리 => 관점지향언어
    public void addInterceptors(InterceptorRegistry registry) { // 로그인 감시, 보내기?. 🔥InterceptorRegistry registry 이게 뭐지?
        registry.addInterceptor(autoLoginInterceptor).order(1) // 자동로그인 // 서버를 껏다가(스프링 서버실행 끄기) 다시 실행 후 다시 페이지로 들어가도 자동로그인 된다.
                .addPathPatterns("/**"); // 범위 지정
        registry.addInterceptor(loginCheckInterceptor).order(2) // 로그인여부 체크 // 인터셉터 추가 // order : 순서
                .addPathPatterns("/user/**") // 패턴 추가
                .excludePathPatterns("/user/login.do") // user 의 login.do 는 제외해라! // exclude : 제외하다
                .excludePathPatterns("/user/signup.do")
                .excludePathPatterns("/user/emailCheck.do")
                .addPathPatterns("/board/**")
                .excludePathPatterns("/board/list.do") // 로그인 제외
                .excludePathPatterns("/board/*/tagList.do") // 로그인 제외
                .excludePathPatterns("/board/*/ajaxTagList.do") // 로그인 제외
                .excludePathPatterns("/board/*/detail.do"); // 로그인 제외
                // 로그인과 회원가입을 제외한 모든 페이지에 갈수없다. => 왜? 인터셉터로 막아서
        registry.addInterceptor(msgRemoveInterceptor).order(3) // 로그인 메세지 삭제
                .addPathPatterns("/**"); // 로그인페이지

//                .addPathPatterns("/") // 인덱스
//                .addPathPatterns("/**"); // 모든경로
//                .addPathPatterns("/user/*detail.do")
//                .addPathPatterns("/user/*/modify.do")
//                .addPathPatterns("/user/*/modify.do")
//                .addPathPatterns("/user/*/modify.do")
//                .addPathPatterns("/user/*/modify.do")
//                .addPathPatterns("/user/*/modify.do");
        // 체크할께 너무 많은 경우 => ** 로
    }
}
