package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.BoardImgDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller // 컨트롤러 설정
@RequestMapping("/board") // 동적페이지 경로에 /board 를 붙이는 설정
//@AllArgsConstructor // 이미지 경로를 객체로 주입받을 때, boardService 객체주입이 오류가 발생될 수 있어서(인지를 못함) 직접 생성자로 주입하기! => public BoardController(){}
@Log4j2
public class BoardController {
    private BoardService boardService; // DIP(의존성 주입 원칙) : 인터페이스 - 확장성이 좋다.

    @Value("${img.upload.path}")
    private String uploadPath; // 등록 (프로젝트위치 + /static/public/img)
    @Value("${static.path}") // 삭제 (imgPath 를 정적 리소스 경로로 하기 때문)
    private String staticPath;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

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

    // 🍋detail 과 수정폼은 같다 => 수정폼은 로그인해야 들어갈수있다!
    @GetMapping("/{bId}/modify.do") // detail 동적페이지
    public String modfiyForm(
            Model model,
            @PathVariable int bId,
            @SessionAttribute UserDto loginUser){ // 로그인 한 사람만 수정할 수 있다
        BoardDto board=boardService.detail(bId); // detail 실행
        model.addAttribute("b",board);
        return "/board/modify";
    }
    @PostMapping("/modify.do")
    public String modifyAction(
            @ModelAttribute BoardDto board, // 보드 번호를 넘겨야 한다!!
            // 이미지가 없을 수도 있어서 required = false // 파라미터로 안받아도 에러 안되게끔
            @RequestParam(value="delImgId", required = false) int [] delImgIds, // 태그 name=delImgId 으로 넘어온 파라미터를 delImgIds 이름으로 받겠다~ // 체크하면 아이디 넘기기
            @RequestParam(value="delImgPath", required = false) String [] delImgPath, // 태그 name=delImgPath 으로 넘어온 파라미터를 delImgIds 이름으로 받겠다~ // 체크하면 아이디 넘기기
            @RequestParam(value="img", required = false) MultipartFile [] imgs //
    ){
        // 🍉db 에서 보드이미지는 지웠는데 참조하는 이미지를 지워야 한다.
        // => 넘긴게 짱구 번호지 짱구 이미지패스가 아니다. => 짱구의 이미지패스가 필요하다.
        // 디비에서 지우기 전에 짱구가가진 이미지패스를 가져와야 한다. 그걸 가지고 삭제를 해야한다.
        // 🍉db 가 아니라 컴퓨터의 파일을 삭제하려면? 패스가 필요하다 => delImgPath
//        log.info(board.getImgs());
        // => [BoardImgDto(biId=32, bId=0, imgPath=null), BoardImgDto(biId=33, bId=0, imgPath=null), BoardImgDto(biId=34, bId=0, imgPath=null)]
//        log.info(board);
        // log.info(Arrays.toString(delImgIds)); // c.a.s.controller.BoardController : [32, 33, 34]
//        |------|-----|------------------------------------------|
//        |bi_id |b_id |img_path                                  |
//        |------|-----|------------------------------------------|
//        |32    |21   |/public/img/board/1681175775732_6481.png  |
//        |33    |21   |/public/img/board/1681175775737_799.jpeg  |
//        |34    |21   |/public/img/board/1681175775739_99439.png |
//        |------|-----|------------------------------------------|
        String redirectPath="redirect:/board/"+board.getBId()+"/modify.do";
//        http://localhost:8080/board/21/modify.do
        int modify=0;
        try{
            // 게시글 수정 + 이미지 수정
            modify=boardService.modify(board,delImgIds);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        if(modify>0){

            redirectPath="redirect:/board/list.do";
        }
        return redirectPath;
    }

    @GetMapping("/register.do")
    public void registerForm( // void : board 의 register.html 을 찾아서 렌더한다.
            @SessionAttribute UserDto loginUser){ // 로그인한 사람만 등록을 할 수 있다.
    }
    @PostMapping("/register.do")
    public String registerAction(
            @SessionAttribute UserDto loginUser, // 글쓴이와 로그인한 사람이 같은지 확인예정
            @ModelAttribute BoardDto board,
            @RequestParam(name="img") MultipartFile [] imgs) throws IOException { // input 태그의 name img 를 imgs 이름으로 받겠다~ // 이미지 등록을 안하면 null 값이 들어온다.
        String redirectPage="redirect:/board/register.do";
        if(!loginUser.getUId().equals(board.getUId())) return redirectPage;  // 다르면 다시 등록페이지로 이동
        // 폼의 글쓴이와 로그인한 사람이 다른 경우 => 예) 다른 브라우저에서 로그아웃하고 다른 아이디로 로그인해서 이전 브라우저의 아이디로 글쓰기 // return 문 실행 후 메서드 종료
//        log.info(board);
//        log.info(img.getOriginalFilename());

        List<BoardImgDto> imgDtos=null;  // 이미지 여러개를 담을 리스트
        if(imgs!=null){ // null 아닌지 체크!
            imgDtos=new ArrayList<>();
            // 이미지가 복수일때 반복문으로 가져오기!
            for(MultipartFile img : imgs){
                if(!img.isEmpty()){ // 이미지가 있으면~
                    String[] contentTypes=img.getContentType().split("/"); // text/xml  application/json  image/png  image/jpg
                    if(contentTypes[0].equals("image")){ // 이미지인지 확인~!
                        String fileName=System.currentTimeMillis()+"_"+(int)(Math.random()*100000)+"."+contentTypes[1];
                        Path path= Paths.get(uploadPath+"/board/"+fileName); // 실질적으로 저장되는 컴퓨터 물리적 위치 // uploadPath : /SpringBoardStudy/src/main/resources/static/public/img
                        img.transferTo(path); // 오류 발생하면 아예 취소해버릴꺼라서 메서드에 500에러 위임! // 임시적으로 저장한 이미지를 물리적 위치로 저장
                        BoardImgDto imgDto=new BoardImgDto();
                        imgDto.setImgPath("/public/img/board/"+fileName); // 서버에 배포되는 경로_db 테이블 이미지 경로
                        imgDtos.add(imgDto);
                    }
                }
            }
        }
        board.setImgs(imgDtos); // null 일 수 도 있고, null 이 아닐수도 있다.
        int register=0;
        try{
            register=boardService.register(board);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        if(register>0){// 등록 성공
            redirectPage="redirect:/board/list.do";
        }else { // 🍉게시글 등록 실패시 저장했던 파일 삭제

        }
        return  redirectPage;
//        register.html 에서 게시글 등록버튼을 눌렀을때, 로그인 유저와 글쓴이 유저가 다르면 다시 등록페이지로 이동되면서 메서드가 종료(컨트롤러에서 아래 코드가 실행이 안되고)
//        유저가 같다면 컨트롤러의 그 아래 코드들이 실행
//        가장아래에 return 되는 페이지는 board/register.do 가 아니라 다른 페이지로 수정할 예정
    }

}
