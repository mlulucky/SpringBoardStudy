package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller // 컨트롤러 설정
@RequestMapping("/board") // 동적페이지 경로에 /board 를 붙이는 설정
@AllArgsConstructor
@Log4j2
public class BoardController {
    private BoardService boardService; // DIP(의존성 주입 원칙) : 인터페이스 - 확장성이 좋다.


    @GetMapping("/list.do")
    public String list(Model model,
                       @SessionAttribute(required = false)UserDto loginUser // 🍉로그인 한사람만 리스트에 들어올수 있으니. 로그인안해도 들어올수 있게 required=false 로
                       ){ // Model 뷰에 객체를 전달
        List<BoardDto> boards;
        if(loginUser==null){  // 🍉로그인 안했을때
            boards=boardService.list();
        }else{
            boards=boardService.list(loginUser.getUId());
        }
//        List<BoardDto> boards=boardService.list();
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

    @GetMapping("/register.do")
    public void registerForm( // void : board 의 register.html 을 찾아서 렌더한다.
            @SessionAttribute UserDto loginUser){ // 로그인한 사람만 등록을 할 수 있다.
    }
    @PostMapping("/register.do")
    public String registerAction(
            @SessionAttribute UserDto loginUser, // 글쓴이와 로그인한 사람이 같은지 확인예정
            @ModelAttribute BoardDto board,
            MultipartFile [] imgs){ // 이미지 등록을 안하면 null 값이 들어온다.
        String redirectPage="redirect:/board/register.do";
        if(!loginUser.getUId().equals(board.getUId())) return redirectPage; // 폼의 글쓴이와 로그인한 사람이 다른 경우
        log.info(board);
        return  redirectPage;
    }

}
