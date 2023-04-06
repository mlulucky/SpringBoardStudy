package com.acorn.springboardstudy.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component // 스프링에서 관리하는 객체
@Log4j2
public class LoginCheckInterceptor implements HandlerInterceptor { // 스프링에서 관리하는 인터셉터 객체
    // 인터셉터 : 필터 보다 기능이 훠얼씬 많이 추가된 미들웨어(==인터셉터)

    // 🍉필터가 가능한곳
    // (컨트롤러(서블릿_동적페이지) 요청 전) // 톰캣용어 - 서블릿  // js 용어? - 컨트롤러
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // return true; // 원래 요청하던 페이지로 이동
        HttpSession session=request.getSession();
        Object loginUserObj=session.getAttribute("loginUser");
        String uri=request.getRequestURI(); // 🍏로그인 성공시 원래 가려했던 페이지로 redirect 예정 // 원래 가려했던 페이지
        if(loginUserObj!=null) { // 로그인 유저가 있으면
            return true; // 페이지 이동 허락
        }else{ // 로그인 유저가 없으면
            session.setAttribute("redirectPage",uri); // 🍏로그인 성공시 원래 가려했던 페이지로 redirect 예정
            session.setAttribute("msg","로그인 시 이용가능한 페이지 입니다.");
            response.sendRedirect("/user/login.do");
            return false; // 페이지 이동을 막는다. // 메소드 종료
        }
    }

    // 🍉요청이 처리가 끝나고 뷰를 렌더하기 직전 ModelAndView
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 🍉view 렌더가 끝나면 (html 을 추가하고 싶을때 _ 알람,알럿창)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
