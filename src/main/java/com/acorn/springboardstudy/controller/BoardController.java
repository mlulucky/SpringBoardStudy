package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.BoardImgDto;
import com.acorn.springboardstudy.dto.PageDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    // public @ResponseBody PageDto list(Model model,
                       @SessionAttribute(required = false)UserDto loginUser, // 🍉로그인 한사람만 리스트에 들어올수 있으니. 로그인안해도 들어올수 있게 required=false 로
                       @ModelAttribute PageDto pageDto // 파라미터 정수 다 있어야지 페이지가 뜬다. 없으면 페이지 에러뜸 http://localhost:8080/board/list.do?page=1&offset=10&order=b_id&direct=asc
                       ){ // Model 뷰에 객체를 전달
//        log.info("pageDto = " + pageDto);
        List<BoardDto> boards;
        boards=boardService.list(loginUser, pageDto); // 페이지를 불러올때, 페이징도 불러오기
        model.addAttribute("page",pageDto);
//        if(loginUser==null){  // 🍉로그인 안했을때
//            boards=boardService.list();
//        }else{
//            boards=boardService.list(loginUser.getUId()); // 로그인한 유저 좋아요한 내역 불러오기
//        }
//        List<BoardDto> boards=boardService.list();
        model.addAttribute("boards",boards); // 뷰에 객체를 전달
        return "/board/list"; // 렌더할 뷰 (board 폴더 안에 list.html)
        // return pageDto; // 🍒jackson 이 파라미터를 가져올때(파싱할때) 다 get 으로 가져온다. => startIndex 만 해도, getStartIndex 가 호출된다.
    }

    @GetMapping("/{tag}/tagList.do")
    public String tagList(
            @PathVariable String tag,
            Model model,
            @SessionAttribute(required = false)UserDto loginUser // 🍉로그인 한사람만 리스트에 들어올수 있으니. 로그인안해도 들어올수 있게 required=false 로
    ){ // Model 뷰에 객체를 전달
        List<BoardDto> boards;
        boards=boardService.tagList(tag,loginUser);
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
    @GetMapping("/{bId}/modify.do") // 게시글 수정폼 메뉴 누르면 링크 이동
    public String modfiyForm(
            Model model,
            @PathVariable int bId,
            @SessionAttribute UserDto loginUser){ // 로그인 한 사람만 수정할 수 있다
        BoardDto board=boardService.detail(bId); // detail 실행
        model.addAttribute("b",board);
        return "/board/modify"; // board 폴더안에 modify html 렌더
    }
    @PostMapping("/modify.do")
    public String modifyAction(
            //🍉클라이언트의 요청정보(파라미터)를 서버에서 처리하기 위해 매개변수로 받는 것
            //🍉클라이언트가 폼에 입력한 값이 파라미터로 넘어오고. 넘어온 입력값(파라미터)을 BoardDto 로 매핑해서 파라미터를 받아오겠다
            @ModelAttribute BoardDto board, //🍉파라미터를 받아오는 것 (✨파라미터를 파싱해준다) // 보드 번호를 넘겨야 한다!! => board.getBId() 사용된다.
            // 이미지가 없을 수도 있어서 required = false // 파라미터로 안받아도 에러 안되게끔

            // @RequestParam(value= "태그의 name")
            // delImgIds 배열이 int 타입인 이유 : input 태그(name="delImgId") 의 value 값(파라미터로 넘어온 값)이 biId, 숫자라서
            // 태그 name=delImgId 으로 넘어온 파라미터를 delImgIds 이름으로 받겠다~ // 체크박스 체크하면 th:value 값 보드이미지아이디가 넘어온다.
            @RequestParam(value="delImgId", required = false) int [] delImgIds, // 삭제할 이미지 // img.biId 여러개
            @RequestParam(value="delImgPath", required = false) String [] delImgPath, // 이미지 삭제에 필요한 이미지경로
            @RequestParam(value="img", required = false) MultipartFile [] imgs, // 새로 등록할 이미지 // 이미지 등록에 사용할 이미지
            @RequestParam(value="tag", required = false) List<String> tags, // 해시태그 등록
            @RequestParam(value="delTag", required = false) List<String> delTags // 해시태그 삭제
    ){
        // 🍉log.info(Arrays.toString(delImgIds)); // 값 잘 넘어오는지 확인 // Arrays.toString : 배열의 출력을 도와주는
        // 🍉db 에서 보드이미지는 지웠는데 참조하는 이미지(컴퓨터에 저장된 경로 Path path= Paths.get(uploadPath+"/board/"+fileName))를 지워야 한다.
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
        String redirectPath="redirect:/board/"+board.getBId()+"/modify.do"; // http://localhost:8080/board/21/modify.do
        List<BoardImgDto> imgDtos=null;

        int modify=0;
        try{
            if(delImgIds!=null) imgDtos=boardService.imgList(delImgIds); // 삭제 전에 이미지 파일 경로를 받아옴
            // 게시글 수정 + 이미지 수정
            modify=boardService.modify(board,delImgIds,tags,delTags);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        // 이미지가 db 에서 삭제가 된후에 파일을 삭제하겠다! ( // 서비스에서 삭제하는 경우에, 트랜잭션으로 원래는 db 는 롤백이 되지만(db 실행을 취소), 파일은 롤백이 안된다.)
        if(modify>0){ // 수정 성공
            if(imgDtos!=null) { // 삭제할 이미지가 있으면
                for(BoardImgDto i : imgDtos){ // 삭제할 목록
                    File imgFile=new File(staticPath+i.getImgPath());
                    if(imgFile.exists()) imgFile.delete();
                }
            }
            redirectPath="redirect:/board/list.do";
        }
        return redirectPath; // 수정 실패
    }

    @GetMapping("/register.do")
    public void registerForm( // void : board 의 register.html 을 찾아서 렌더한다.
            @SessionAttribute UserDto loginUser){ // 로그인한 사람만 등록을 할 수 있다.
    }
    @PostMapping("/register.do")
    public String registerAction(
            @SessionAttribute UserDto loginUser, // 글쓴이와 로그인한 사람이 같은지 확인예정
            @ModelAttribute BoardDto board,
            @RequestParam(name="img", required = false) MultipartFile [] imgs, // 이미지가 없어도 받겠다. // input 태그의 name img 를 imgs 이름으로 받겠다~ // 이미지 등록을 안하면 null 값이 들어온다.
            @RequestParam(name="tag", required = false) List<String> tags) throws IOException {
        String redirectPage="redirect:/board/register.do"; // @GetMapping("/register.do")
        // 해시태그 테스트
//        System.out.println("tags = " + tags);
//        if(tags!=null) return redirectPage;

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
            register=boardService.register(board,tags);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        if(register>0){// 등록 성공
            redirectPage="redirect:/board/list.do";
        }else { // 🍉게시글 등록 실패시 저장했던 파일 삭제
            if(imgDtos!=null){
                for(BoardImgDto imgDto : imgDtos) {
                    File imgFile=new File(staticPath+ imgDto.getImgPath());
                    if(imgFile.exists()) imgFile.delete();
                }
            }
        }
        return  redirectPage;
//        register.html 에서 게시글 등록버튼을 눌렀을때, 로그인 유저와 글쓴이 유저가 다르면 다시 등록페이지로 이동되면서 메서드가 종료(컨트롤러에서 아래 코드가 실행이 안되고)
//        유저가 같다면 컨트롤러의 그 아래 코드들이 실행
//        가장아래에 return 되는 페이지는 board/register.do 가 아니라 다른 페이지로 수정할 예정
    }

    @GetMapping("/{bId}/remove.do")
    public String removeAction(
            @PathVariable int bId,
            @SessionAttribute UserDto loginUser,
            RedirectAttributes redirectAttributes){
        String redirectPath="redirect:/board/"+bId+"/modify.do";
        String msg="삭제 실패"; // 리다이렉트 하는 페이지에 메세지를 전달
//        redirectAttributes.addAttribute("msg",msg); // http://localhost:8080/board/23/modify.do?msg=%EC%82%AD%EC%A0%9C+%EC%8B%A4%ED%8C%A8
        BoardDto board=null;
        List<BoardImgDto> imgDtos=null;
        int remove=0;
        try{
            board=boardService.detail(bId); // 이땐 lazy 라서 이미지가 없었다. // 이미지 삭제하려고 불러오기
            imgDtos=board.getImgs(); // getImgs 이미지를 호출하는 트리거. 조인이 lazy 이면 호출해서 불러온다.
            remove=boardService.remove(bId); // 디비에서 삭제
        }catch (Exception e){
            log.error(e);
        }
        if(remove>0){ // 게시글 삭제 성공
            if(imgDtos!=null){
                for (BoardImgDto i : imgDtos){
                    File imgFile=new File(staticPath+i.getImgPath());
                    if(imgFile.exists()) imgFile.delete(); // 이미지 실제 삭제
                }
            }
            msg="삭제 성공!";
            redirectPath="redirect:/board/list.do";
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return redirectPath;
    }
}
