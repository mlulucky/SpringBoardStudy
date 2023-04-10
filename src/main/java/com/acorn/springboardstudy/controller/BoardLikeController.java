package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardLikeDto;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.BoardLikeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board/like") // board/like 폴더 안에있는것처럼 가상경로를 붙여주기~!
@AllArgsConstructor
@Log4j2
public class BoardLikeController {
    private BoardLikeService boardLikeService;

    // http://localhost:8080/board/like/1/read.do
    @GetMapping("/{bId}/read.do")
    public String readLikeStatusCnt(
            @PathVariable int bId,
            @SessionAttribute(required = false) UserDto loginUser,
            Model model) {
        String templatePage;
//        String templatePage="";
        LikeStatusCntDto likes;
        model.addAttribute("id",bId); // 뷰에 bId 객체 전달
        if (loginUser != null) {
            likes = boardLikeService.read(bId, loginUser.getUId());
            templatePage="/board/loginLikes";
        } else { // null 이면
            likes = boardLikeService.read(bId);
            templatePage="/board/likes";
        }
        // log.info(likes);  // LikeStatusCntDto(like=1, sad=0, bad=1, best=0, status=BAD)
//        LikeStatusCntDto likes=new LikeStatusCntDto();
        model.addAttribute("likes", likes);
        return templatePage;
//        return "/board/likes";
    }

    @Data
    class HandlerDto {
        // 좋아요 등록,수정,삭제
        enum HandlerType {REGISTER, MODIFY, REMOVE}
        private HandlerType handlerType; // 🔥이건 왜 필요한건가?
        private String status;
        int handler; // 0 실패, 1 성공
    }

    // 좋아요 등록, 취소, 삭제를 할 수 있다.
    // 1번글 좋아요를 누르면 => /board/like/LIKE/1/handler.do
    // {"handlerType":"MODIFY","status":"LIKE","handler":1}
    // 3번글 슬퍼요를 누르면 => /board/like/SAD/3/handler.do
    @GetMapping("/{status}/{bId}/handler.do") // 좋아요싫어요를 bid 몇번 글에 할것인가.
    public @ResponseBody HandlerDto handler(
            @PathVariable String status,
            @PathVariable int bId,
            @SessionAttribute UserDto loginUser) { // 로그인한 사람만 좋아요 등록가능
        HandlerDto handlerDto = new HandlerDto();
        handlerDto.setStatus(status);

        // 로그인 유저가 좋아요했는지 내역을 가져오는 것
        BoardLikeDto boardLike=boardLikeService.detail(bId,loginUser.getUId());
        int handler=0;
        BoardLikeDto like=new BoardLikeDto();
        like.setStatus(status);
        like.setUId(loginUser.getUId());
        like.setBId(bId);
        if(boardLike==null){ // 좋아요 첫번째 등록
            handlerDto.setHandlerType(HandlerDto.HandlerType.REGISTER);
            handler=boardLikeService.register(like);
        }else{ // 수정 또는 삭제
            if(boardLike.getStatus().equals(status)) { // 삭제(취소) - 상태가 같을때
                // 좋아요가 눌렸는데 다시 좋아요를 누른 것
                handlerDto.setHandlerType(HandlerDto.HandlerType.REMOVE);
                handler=boardLikeService.remove(like);
            }else { // 수정) 좋아요를 눌렀는데 싫어요를 누른 것
                handlerDto.setHandlerType(HandlerDto.HandlerType.MODIFY);
                handler=boardLikeService.modify(like);
            }
        }
        handlerDto.setHandler(handler);
        //handlerDto.setHandlerType(HandlerDto.HandlerType.REGISTER); // HandlerDto.HandlerType.REGISTER 인
        return handlerDto;
        // log.info(status);
        // log.info(bId);
    }


}
