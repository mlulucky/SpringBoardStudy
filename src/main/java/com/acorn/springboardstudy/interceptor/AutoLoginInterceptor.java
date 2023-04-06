package com.acorn.springboardstudy.interceptor;

import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.lib.AESEncryption;
import com.acorn.springboardstudy.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // 스프링 빈(객체)으로 주입을 받기 위해서 컴포넌트로 등록
@AllArgsConstructor
@Log4j2 // 로그의 관심사가 분리되어있어서 어노테이션으로 주입받을 수 있는 것
public class AutoLoginInterceptor implements HandlerInterceptor {
    // 로그인을 한 사람을 필요가 없는 기능(자동로그인). 로그인 안한 사람에게만 적용

    private UserService userService; // 스프링컨테이너에서 서비스 주입 및 객체 주입 // @Component 로 되어있으면 객체 주입받을 수이싿.

    // preHandle 인터셉터 : 컨트롤러(서블릿_동적페이지) 요청 전 체크하는 미들웨어
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // return true;  // 원래 요청하던 페이지로 이동
        HttpSession session=request.getSession(); // 세션에 저장
        Object loginUserObj=session.getAttribute("loginUser");
        if(loginUserObj!=null) return true;


        // 클라이언트(브라우저)는 쿠키를 요청에 담아서 서버로 보낸다.
        Cookie[] cookies=request.getCookies(); // 쿠키가 배열로 넘어온다.

        // 쿠키가 아예 없을 수도 있다.
        if(cookies==null) return true; // 쿠키가 없으면 자동로그인..?
        Cookie loginId=null;
        Cookie loginPw=null;

        for(Cookie c : cookies) {
            if(c.getName().equals("SPRING_LOGIN_ID")) {
                loginId=c;
            }else if(c.getName().equals("SPRING_LOGIN_PW")){
                loginPw=c;
            }
        }

        // 쿠키가 둘중(아이디,비번) 하나라도 없을때가 있을수 있어서?! 둘다잇을때만 쿠키로
        if(loginId!=null && loginPw!=null){
            UserDto loginUser=null;
            String msg=null; // 메세지 보내기위해서 변수설정
            try{
                UserDto user=new UserDto(); // 유저에게 쿠키의 아이디와 비번을 넘긴다.
                // 복호화 아이디: ynjFUVAtbk6TWUwKEoGm1A== (해시코드) => "user01" (평문)
                // 복호화 비번: Wxnal5f0WQOLQAXPArZNyg== => "1234"
                // JSSESSIONID : 세션객체의 유효시간을 만료시킨다(초기화) // 브라우저에 유효 아이디값을 준다. // 만료시간이 되면 브라우저가 지운다.
                user.setUId(AESEncryption.decryptValue(loginId.getValue())); // decryptValue : 복호화(암호-> 평문) // 로그인을 하려면 복호화가 되있어야 한다. // 길이가 동일한 암호화 코드 - 해쉬코드
                user.setPw(AESEncryption.decryptValue(loginPw.getValue()));
                loginUser=userService.login(user);
            }catch (Exception e){
                log.error(e.getMessage());
            }
            if(loginUser!=null){ // 로그인 성공
                msg="자동 로그인 성공";
                session.setAttribute("loginUser",loginUser); // 세션에 로그인유저 저장
                session.setAttribute("msg",msg);
//                msg="자동로그인 성공";
                return true;// 로그인 성공하면 가던길 갈거라서
            }else {
                msg="자동로그인 실패 (쿠키 정보를 삭제합니다. 다시 로그인하세요.)"; // 자동로그인에 필요한 정보(아이디, 비밀번호)가 유효하지 않을때

                // 유효한 쿠키가 아니라서 다 지우기!
                loginId.setMaxAge(0); // 로그인 아이디 쿠키 삭제 (만료시간 0)
                loginPw.setMaxAge(0);
                loginId.setPath("/"); // 🔥 setPath 란? setPath 와 sendRedirect 차이 ?
                loginPw.setPath("/");
                response.addCookie(loginId); // 다시 새로운 쿠키를 응답에 포함해서 보낸다.
                response.addCookie(loginPw);
                session.setAttribute("msg",msg); // msg 내용이 너무 길어서 따로 뺌

                response.sendRedirect("/user/login.do"); // 다시 로그인 페이지로 가라
                return false;
            }
        }

        return true; // true 는 자동로그인을 안하는것! 가던길 가라~!
    }
}
