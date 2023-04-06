package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardReplyDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.mapper.BoardReplyMapper;
import com.acorn.springboardstudy.service.BoardReplyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/reply") // reply 경로를 추가하기 - 하위에 있는것처럼 가상경로
@Log4j2
//@AllArgsConstructor //
public class ReplyController {
    // private BoardReplyMapper boardReplyMapper; // @AllArgsConstructor : 객체를 생성 + 객체를 주입
    private BoardReplyService boardReplyService; // 컴포넌트로 주입받는 것

    @Value("${img.upload.path}") // 경로는 @value 로 주입받는 것. //application.yml 설정값 가져오기 // @Value : 스프링 빈 팩토리 어노테이션
    private String imgUploadPath;

    // 이미지패스 경로 private String imgUploadPath; 는 생성자로 주입받지 않고 @Value 로 따로 주입받기때문에, @AllArg 로 생성자 주입받으면 컴포넌트의 생성자로 주입받기 때문에
    // @AllArg 빼고 boardReplyService 서비스는 따로 생성자 구현해서 주입받음
    public ReplyController(BoardReplyService boardReplyService) {
        this.boardReplyService = boardReplyService;
    }


    @GetMapping("/{brId}/detail.do")
    public @ResponseBody BoardReplyDto detail(@PathVariable int brId){ // @ResponseBody : (getter/setter 로 정의된)자바 객체 BoardReplyDto를 json 으로 파싱해준다. // BoardReplyDto 를 responsebody 로 보내겠다.
        BoardReplyDto reply=boardReplyService.detail(brId);
//        return new BoardReplyDto();
//        return boardReplyService.detail(brId);
        log.info(reply);
        return reply;
    }


    // http://localhost:8080/reply/1/list.do
    @GetMapping("/{bId}/list.do")
        public String list(@PathVariable int bId,
                           Model model){
        List<BoardReplyDto> replies=boardReplyService.list(bId);
        model.addAttribute("replies",replies);
            return "/reply/list";
        }

    @Data
    class HandlerDto{
        private int register;
    }

    // @ResponseBody : view 를 응답하지 않고 해당 객체를 json 으로 파싱해서 배포
    @PostMapping("/handler.do")
    public @ResponseBody HandlerDto registerHandler(
            @ModelAttribute BoardReplyDto reply,
            @SessionAttribute UserDto loginUser, // 로그인 한 사람만 댓글을 작성할 수 있다.
            MultipartFile img
        ) throws IOException {
        log.info(reply);
        log.info(img.getOriginalFilename());
        HandlerDto handlerDto=new HandlerDto();
        if(!img.isEmpty()){
            String[] contentsTypes=img.getContentType().split("/");
            if(contentsTypes[0].equals("image")){
                String fileName=System.currentTimeMillis()+"_"+(int)(Math.random()*10000)+"."+contentsTypes[1];
                Path path=Paths.get(imgUploadPath+"/reply/"+fileName);
                img.transferTo(path); // fetch 에서 resp.status 200 일때만 처리하기 때문에 그냥오류가 발생하면 500
                reply.setImgPath("/public/img/reply/"+fileName);
            }
        }
        int register=boardReplyService.register(reply); // 500
        handlerDto.setRegister(register);
        return handlerDto;
    }

    // 댓글 등록 - 등록 실패, 성공해도 댓글상세 페이지로 돌아온다.
    @PostMapping("/insert.do")
    public String insertAction(
            @ModelAttribute BoardReplyDto reply, // @ModelAttribute : 명시적인것. 파라미터로 객체를 보내겠다라는 뜻
            RedirectAttributes redirectAttributes,
            @SessionAttribute UserDto loginUser, // 로그인 유저가 아니면 에러
            MultipartFile img // 파일타입의 input 태그 name 이 img 인것 // 🔥 // 이미지가 복수인경우에는 배열로 받으면 된다. MultipartFile[]  // 임시저장소에 잠시 대기하다가 아무것도 하지않으면 삭제 // form 태그의 name img
            ) {
        int register=0;
        String msg="댓글 등록실패";
//        log.info(imgUploadPath);
        // /Users/moon/eunjeong/webAppStudy20230117/SpringBoardStudy/src/main/resources/static/public/img
        try{
            // 등록하기 전에 이미지 등록
            if(img!=null && !img.isEmpty()) { // img 파일을 선택하지 않아도 빈값(null- 객체없음)이 오지 않는다. // isEmpty : 객체는 있는데 파일이 없는 경우
                String contentType=img.getContentType(); // 🔥 image/png or image/jpeg or text/xml or application/json
                log.info(contentType);
                String [] contentTypes=contentType.split("/");
                if(contentTypes[0].equals("image")){ // image
                    String fileName=System.currentTimeMillis()+"_"+(int)(Math.random()*100000)+"."+contentTypes[1]; // png or jpeg
                    String imgPath=imgUploadPath+"/reply/"+fileName; // 물리적으로 서버컴퓨터에 저장되는 위치 // 또 쓸거라서 변수로 저장
                    Path path= Paths.get(imgPath); // 컴퓨터 위치를 저장하는것
                    img.transferTo(path); // 물리적으로 저장하는거 🔥스프링보드스터디 폴더에 이미지 저장하는 거       // 이미지 저장 통신에 오류가 생길수있어 예외처리된다.
                    reply.setImgPath("/public/img/reply/"+fileName);  // 사용자의 파일경로를 디비에 저장하려면 경로를 지정하는거 // 서버가 이미지를 배포하는 위치 (클라이언트가 이미지를 등록하는 위치)
                }
                // System.currentTimeMillis 현재시간 밀리세컨즈
            }
            register=boardReplyService.register(reply);
        }catch (Exception e){
            log.error(e.getMessage());
            msg+="에러 : " + e.getMessage();
        }
//        log.info(reply);
        if(register>0){
            msg="댓글 등록 성공";
        }
        redirectAttributes.addFlashAttribute("msg",msg); // 리다이렉트 페이지에 메시지 보내기
        return "redirect:/board/"+reply.getBId()+"/detail.do";
    }
}
