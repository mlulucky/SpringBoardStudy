package com.acorn.springboardstudy.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MsgRemoveInterceptor implements HandlerInterceptor {
    // 로그인 모달 메세지 삭제하기
    // redirect addFlashAttribute 와 같다.
    // 🍉왜 따로 만들었나? 인텁세터의 preHandler 에서 리다이렉트를 쓸수 없어서 메세지를 보낼수 없어서! 이렇게 따로 분리
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 메세지가 있으면 렌더가 끝난상태니까. 다 지워버리기
        // 🍉view 렌더가 끝나면 (html 을 추가하고 싶을때 _ 알람,알럿창)
        HttpSession session=request.getSession();
//        // 이미 뷰에서 모달을 렌더한 상황 => 모달 한번 띄워지면 이후는 지우기
        if(session.getAttribute("msg")!=null){
            session.removeAttribute("msg");
        }
    }
}
