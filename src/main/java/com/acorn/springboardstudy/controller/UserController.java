package com.acorn.springboardstudy.controller;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor // 🍒객체를 주입 // 🍒모든 필드를 POJO 형식의 생성자로 자동 생성 (컴파일할때! 왜? 어노테이션은 컴파일할때 실행)
@Controller // 요청과 응답을 처리하는 기능 // @Component 의 일종(@Component 부모 / @Controller 자식)
@RequestMapping("/user") // 🍒전체적인 url 설정 // 현재 파일의 동적리소스의 경로는 모두 /user 경로가 붙는다. (/user 하위 경로인것 처럼)
@Log4j2 // 🍏log 필드로 로그(log)를 남길 수 있다. (파일로 저장 가능[유지(저장)기간,성질( ex)에러 로그만)])
// 어노테이션으로 주입을 받는다.
// => (관심사(로그,서비스,컨트롤러)가 다 분리되어있어서 로그만 주입받을 수 있다.)
// **System.out.print() => 휘발성 로그 (콘솔에 출력만 가능 - 남길 수 없다.)


public class UserController {
    // 🍒동적페이지 - @GetMapping, @PostMapping 으로 정의된 함수

    private UserService userService;
//    public UserController(UserService userService) { // 생성자
//        this.userService = userService;
//    }

    // 유저 서비스(객체)를 주입받고 싶다! -> 방법) 1. @Autowired 어노테이션 사용 2. POJO 방식의 생성자
    // 🍒여기서 생성자로 주입안한 이유 ? @Autowired 안하는 이유 ?? => @AllArgsConstructor
    // 구조메뉴 보면 -> UserController(UserService) 생성되있음.

    // 🍒url 로 요청하는 것은 모두 GET 방식이다!!!
    // 🍒*GET 을 제외한 다른 메서드( ex) POST )는 양식을 제출하거나 ajax 로 페이지를 호출할때만 가능
    // POST : 값을 입력 또는 체크한것을 버튼을 눌렀을대 네트워크 페이로드에 값이 전달이 된다.

    // GET 로그인 - 화면렌더
    // "/user/login.do" 동적페이지 정의
    @GetMapping("/login.do")
    public String loginForm(){
        return "/user/loginForm"; // 렌더할 뷰(html) / 반환타입 String
    }

    // POST 로그인 // form 태그의 버튼 클릭시 네트워크의 페이로드에 input 입력 또는 체크한 값 (파라미터)이 넘어간다
    @PostMapping("/login.do")
    public String loginAction(
            UserDto user, // UserDto 안에 uId 와 pw 가 있어서 uId 와 pw 파라미터를 UserDto user 로 대체할 수 있다.
            Integer autoLogin, // 자동로그인에 사용예정 // int 타입의 래퍼클래스인 Integer 는 자료형으로 null 값을 갖는다.
            // int autoLogin, // 🍒input 체크박스를 체크안하면 null 이 넘어가는데, int 는 null 을 파싱할 수 없어서 에러가 발생!
            // => 자동로그인 체크박스 체크시 - ok / 🍒체크 안할 시 int 타입인 경우 페이지 에러가 뜬다. // int 는 null 을 파싱할 수 없어서 Integer 타입으로
            HttpSession session, // 톰캣 로그인 // session : 서버에 저장되는 정보
            RedirectAttributes redirectAttributes){
        // 🍒2. 파라미터 받는법 getter,setter 가 정의된 Dto(파라미터의 필드가 모두 포함된) 를 받는다. => UserDto user
        // @RequestAttribute 를 하게되면(@RequestAttribute UserDto user) Dto 에 있는 필드를 모두 파라미터로 받아야해서 설정이 더 필요해서 안쓰는 편
        // @RequestAttribute == @RequestParam
        // @RequestParam(required = true) 설정이 기본적으로 포함된 것
        // session 로그인할때 서버에 저장되는 정보 // 톰캣에 session 이 가져와진다.

        // 🍒1. 파라미터 받는법 (매개변수로 파라미터와 똑같은 필드명과 타입을 받는다)
        // @RequestParam(name="uId", required = true) String uId,
        // String pw){ // required=true (기본값) 파라미터가 없으면 400 에러를 띄워라

        // 🍏redirect 페이지에 로그인성공, 로그인 실패 메세지를 전달하는 방법 - 2가지
        // 1. 파라미터로  ?msg=로그인 성공"; (권장x)  => 🍒파라미터가 전달된다.(redirectAttributes.addAttribute)
        // => redirectAttributes.addAttribute("msg","로그인성공"); // http://localhost:8080/?msg=%EB%A1%9C%EA%B7%B8%EC%9D%B8%EC%84%B1%EA%B3%B5
        // 2. Session 에 추가한 후에 사용하고 삭제 (권장o - 보안)
        // 🍒3. RedirectAttributes (자동으로 세션에 추가 및 삭제해주는 것)
        // => redirectAttributes.addFlashAttribute("msg","로그인성공"); 세션에 저장되었다가 사용하면 바로 삭제
        UserDto loginUser=null;
         // log.info(user); // log - @Log4j2
         // log.info(autoLogin); // 🍒자동로그인 체크시 1(value 값) , 체크안하면 null // log - @Log4j2
//        2023-04-01T01:50:14.333+09:00  INFO 7937 --- [nio-8080-exec-1] c.a.s.controller.UserController
//        : UserDto(uId=user01, pw=1234, name=null, phone=null, imgPath=null, email=null, postTime=null, birth=null, gender=null, address=null, detailAddress=null, permission=null)
//        2023-04-01T01:50:14.333+09:00  INFO 7937 --- [nio-8080-exec-1] c.a.s.controller.UserController          : 1

        try {
            loginUser=userService.login(user);
        }catch(Exception e){ // 오류 뜰 가능성
            log.error(e.getMessage()); // log 주입 - @Log4j2
        }
        if(loginUser!=null){ // 유저가 null 이 아니면
            redirectAttributes.addFlashAttribute("msg","로그인성공"); // 세션에 저장되기 때문에 파라미터에 보이지 않는다. 이걸 이용해서 모달창으로 띄울 수 있다.! // 세션에 저장되었다가 사용하면 바로 삭제
            session.setAttribute("loginUser",loginUser); // // session 객체에 loginUser 속성을 저장
            return "redirect:/"; // 인덱스 페이지 // redirect : 처리페이지(서버 내에서 페이지 호출) - Get 방식

        }else{ // 로그인유저가 null 이면 (로그인 실패)
            redirectAttributes.addFlashAttribute("msg","아이디나 패스워드를 확인하세요!"); // 세션에 저장되었다가 사용하면 바로 삭제
            return "redirect:/user/login.do"; // 로그인페이지 호출
        }
    }

    // -- end 로그인


    @GetMapping("/{uId}/detail.do") // 헐.. ㅜ 경로를 계속 /user/ 를 붙여서 에러가 났음..
    public String detail(@PathVariable String uId, Model model){
        UserDto user=null;
        try{
            user=userService.detail(uId);
            model.addAttribute("user", user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/user/detail";
    }

    @GetMapping("/signup.do")
    public void signupForm(){
        // return signup.html
    }

    @PostMapping("/signup.do")
    public String signupAction(UserDto user, HttpSession session, RedirectAttributes redirectAttributes){
        int insert=0;
        String modalMsg="";
        String errorMsg=null;
        try{
            insert=userService.signup(user);
        }catch(Exception e){
            e.printStackTrace();
            errorMsg=e.getMessage();
        }
        if(insert>0){
            return "redirect:/";
        }else {
            modalMsg="에러:"+errorMsg;
//            session.setAttribute("actionMsg",modalMsg);
            redirectAttributes.addFlashAttribute("회원가입 에러",modalMsg);
            return "redirect:/user/signup.do";
        }
    }





}
