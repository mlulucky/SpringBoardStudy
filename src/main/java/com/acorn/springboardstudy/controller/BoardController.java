package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller // 컨트롤러 설정
@RequestMapping("/board") // 동적페이지 경로에 /board 를 붙이는 설정
@AllArgsConstructor
public class BoardController {
    private BoardService boardService; // DIP(의존성 주입 원칙) : 인터페이스 - 확장성이 좋다.

    @GetMapping("/list.do")
    public String list(Model model){ // Model 뷰에 객체를 전달
        List<BoardDto> boards=boardService.list();
        model.addAttribute("boards",boards); // 뷰에 객체를 전달
        return "/board/list"; // 렌더할 뷰 (board 폴더 안에 list.html)
    }

    //  ?bId=1  파라미터를 쿼리스트링으로 보내는건 올드
    // "/{bId}/detail.do" : @PathVariable :  패스에다가 파라미터를 보내는 것 // bId 가 동적페이지에 꼭 필요하다는 것을 명시적으로 나타내는 것
    @GetMapping("/{bId}/detail.do") // detail 동적페이지
    public String detail(Model model, @PathVariable int bId){
        BoardDto board=boardService.detail(bId); // detail 실행
        model.addAttribute("b",board);
        return "/board/detail";
    }

}
