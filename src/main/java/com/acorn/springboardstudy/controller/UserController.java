package com.acorn.springboardstudy.controller;
import com.acorn.springboardstudy.dto.EmailDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.lib.AESEncryption;
import com.acorn.springboardstudy.service.EmailService;
import com.acorn.springboardstudy.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.*;
import java.security.SecureRandom;
import java.util.Base64;

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
    private EmailService emailService; // 생성자

//    public UserController(UserService userService) { // 생성자
//        this.userService = userService;
//    }

    // 유저 서비스(객체)를 주입받고 싶다! -> 방법) 1. @Autowired 어노테이션 사용 2. POJO 방식의 생성자
    // 🍒여기서 생성자로 주입안한 이유 ? @Autowired 안하는 이유 ?? => @AllArgsConstructor
    // 구조메뉴 보면 -> UserController(UserService) 생성되있음.

    // 🍒url 로 요청하는 것은 모두 GET 방식이다!!!
    // 🍒*GET 을 제외한 다른 메서드( ex) POST )는 양식을 제출하거나 ajax 로 페이지를 호출할때만 가능
    // POST : 값을 입력 또는 체크한것을 버튼을 눌렀을대 네트워크 페이로드에 값이 전달이 된다.


    // http://localhost:8080/user/user01/detail.do (PathVaribale)
    // 로그인한 사람만 페이지에 접근가능하게 하고 싶다.
    // 방법1. filter(intercepter) : 해당 페이지를 요청하기 전에 로그인 했는지 검사(session 에 해당 유저가 있는지 검사)
    // 방법2. controller : 해당 페이지에서 로그인 했는지 검사(session 에 해당 유저가 있는지 검사)
    @GetMapping("/dropout.do")
    public String dropoutForm(
            @SessionAttribute UserDto loginUser){
        return "/user/dropoutForm";
    }
    @PostMapping("/dropout.do") // 회원탈퇴 + 로그아웃
    public String dropoutAction(
            @ModelAttribute UserDto user,
            @SessionAttribute UserDto loginUser,
            RedirectAttributes redirectAttributes,
            HttpSession session){
        String msg="회원탈퇴 실패 (비밀번호 확인)";
        String redirectpage="redirect:/user/dropout.do";
        int dropout=0;
        try{
            dropout=userService.dropout(user);

        }catch (Exception e){
            log.error(e.getMessage());
            msg+="에러 : " +e.getMessage();
        }
        if(dropout>0){
            msg="이용해주셔서 감사합니다.(회원탈퇴 성공)";
            redirectpage="redirect:/";
            session.removeAttribute("loginUser");
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return redirectpage;
    }


    @GetMapping("/{uId}/modify.do") // 유저아이디 + 로그인여부
    public String modify(@PathVariable String uId, // @PathVariable 은 생략할수없다.
                         @SessionAttribute UserDto loginUser, // 로그인이 안되어있으면 페이지에 접근불가. 400 에러발생
                          Model model){ // Model : 렌더할 뷰에 바로 객체 전달
        UserDto user=userService.detail(uId,null); // 수정할때, 팔로잉 리스트 필요없어서 null 보냄
        model.addAttribute("user",user);
        return "/user/modify"; // 렌더시, 페이지에 유저가 존재
    }

    @PostMapping("/modify.do")
    public String modifyAction( // 로그인 한 사람만
            @SessionAttribute UserDto loginUser,
            @ModelAttribute UserDto user,
            RedirectAttributes redirectAttributes){ // UserDto 로 파라미터 받기
        int modify=0;
        String msg="수정 실패";
        String redirectPage="redirect:/user/"+user.getUId()+"/modify.do";
        try{
            modify=userService.modify(user);
        }catch (Exception e){
            log.error(e.getMessage());
            msg+="에러 : "+e.getMessage(); // 수정실패, 에러메리시지
        }

        if(modify>0){
            redirectPage="redirect:/user/"+user.getUId()+"/detail.do";
            msg="수정성공";
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return redirectPage;
    }

    @GetMapping("/{uId}/detail.do")
    public ModelAndView detail( // ModelAndView 는 예전에 쓰던것(코드복잡). 요새는 String(코드 간단) 많이 쓴다.
            @SessionAttribute(required = false) UserDto loginUser,
            // UserDto loginUser = (UserDto) session.getAttribute("loginUser")
            // 세션객체를 파라미터로 인지해서, 세션이 없으면
            // 세션객체를 파라미터 취급(required=true) 해서 없으면 400 에러
            // => 파라미터를 없을수 도 있다~ 라는 처리를 해주기(로그인 유저가 없을수도 있고, 없어도 에러 발생안되게끔) required=false (로그인 안하는 경우)

            @PathVariable String uId,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes
            ){ // ModelAndView : 렌더하는 뷰 설정 + 뷰(html)에 객체를 전달

        if(loginUser==null){ // 로그인유저가 없을때(로그인 안했을때)
            redirectAttributes.addFlashAttribute("msg","로그인을 해야 이용할수 있는 페이지입니다.");
            modelAndView.setViewName("redirect:/user/login.do");
            return modelAndView;
        }
        String loginUserId=(loginUser!=null)?loginUser.getUId():null;
        UserDto user=userService.detail(uId,loginUserId);
        modelAndView.setViewName("/user/detail"); // 1. 뷰를 렌더할때
        modelAndView.addObject("user",user); // 2. user 객체(model)를 쓰겠다.


        return modelAndView; // 모델+뷰 를 렌더
//        return "/user/detail"; // 뷰(html) 렌더 (.html 생략가능)
    }

    @GetMapping("/signup.do")
    public void signupForm(){
    }

    @GetMapping("/emailCheck.do")
    public void emailCheckForm(@RequestParam String uId){ // url 파라미터. required true // 파라미터 uId 가 없으면 못들어온다.

    }
    @PostMapping("/emailCheck.do")
    public String emailCheckAction(
            UserDto user,
            RedirectAttributes redirectAttributes){
            String msg="";
        String redirectPath="";
        int emailCheck=0;
        try{
            user.setStatus(UserDto.StatusType.SIGNUP);
            emailCheck= userService.modifyEmailCheck(user);
        }catch (Exception e) {
            log.error(e.getMessage()); // 로그 사용하는 이유 : 로그를 파일로 저장할수있다. => log4j
        }

        if(emailCheck>0) { // 보낸코드와 생성된 코드가 같은 경우
            msg="이메일이 확인되었습니다.(회원가입 완료) 로그인하세요";
            redirectPath="redirect:/user/login.do";
        }else {
            msg="이메일로 보낸 코드를 다시 확인하세요";
            redirectPath="redirect:/user/emailCheck.do";
            redirectAttributes.addAttribute("uId", user.getUId());
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return redirectPath;
    }

    @PostMapping("/signup.do")
    public String signupAction(@ModelAttribute UserDto user,
        RedirectAttributes redirectAttributes){ // 성공실패 메세지 보내기위한 파라미터
        // @ModalAttribute :
        // @RequestAttribute : 리퀘스트 객체를 모두 가져오는 것 (쿠키,세션, url)
        log.info(user.toString()); // 로그는 toString() 쓰는것을 권장
        String errorMsg=null;
        int signup=0;

        try{
            // 난수 생성
            SecureRandom random=new SecureRandom(); // 바이트인코딩으로 랜덤한 난수로 생성
            byte[] bytes=new byte[6]; // 6자리 바이트생성
            random.nextBytes(bytes); // 바이트에 랜덤코드 생성
            // 이메일 체크코드
            String emailCheckCode=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // 바이트 인코딩을 문자열로 바꿔준다.
            user.setEmailCheckCode(emailCheckCode);
            user.setStatus(UserDto.StatusType.EMAIL_CHECK); // 유저 상태를 체크 // 외부에서 참조해서 쓸려면 enum StatusType 이 public 이어야 한다.
            signup=userService.signup(user);
            if(signup>0){ // 회원가입이 성공하면 이메일을 확인
                EmailDto emailDto=new EmailDto();
                emailDto.setTo(user.getEmail()); // 유저에게 메일 보내기
                emailDto.setTitle("찹찹 가보자고 가입 이메일 확인 코드입니다."); // 보내는 메일 내용
                emailDto.setMessage("<h1>해당코드를 입력하세요.</h1><h2>CODE : "+emailCheckCode+"</h2>"); // emailCheckCode : 방금생성한 체크코드
                emailService.sendMail(emailDto);

                // 중요한 내용은 숨기기
                redirectAttributes.addFlashAttribute("msg","이메일을 확인해야 회원가입을 성공합니다.");
                // return "redirect:/user/emailCheck.do?uId="+user.getUId();
                redirectAttributes.addAttribute("uId",user.getUId()); // == ?uId="+user.getUId(); // ? 파라미터 뒤에 자동으로 uId 를 붙여준다.
                return "redirect:/user/emailCheck.do";
            }

        }catch (Exception e){
            log.error(e); // 로그4j 로 파일로 저장할 수 있다.
            errorMsg=e.getMessage(); // 상세하게 하라고 했지 유저에게 데이터베이스의 에러 내용까지 알려줄 필요는 없다.
        }
        redirectAttributes.addFlashAttribute("msg","회원가입 실패 에러 : " + errorMsg);
            return "redirect:/user/signup.do";

    }

    // 로그아웃 동적 페이지
    @GetMapping("/logout.do") // 원래 서버를 재시작(새로고침)하면 무조건 로그아웃된다.
    public String logoutAction(
            HttpSession session, // 세션객체
            RedirectAttributes redirectAttributes, // 세션 메세지 보내기 위해서 사용
            @CookieValue(value="SPRING_LOGIN_ID", required = false) String loginIdVal, // 쿠키의 value 이기 때문에 key 값을 적어야 한다.
            @CookieValue(value="SPRING_LOGIN_PW" ,required = false) String loginPwVal, // required = false : 자동로그인 체크 안하고 로그인하고 다시 로그아웃할때 에러 없애는 법
            HttpServletResponse resp
            ){
//        session.invalidate(); // 로그아웃 // 세션 전체 제거 (서버재시작은 무조건 로그아웃)
          if(loginIdVal!=null || loginPwVal!=null) {
              Cookie loginId=new Cookie("SPRING_LOGIN_ID", "");
              Cookie loginPw=new Cookie("SPRING_LOGIN_PW", "");
              loginId.setMaxAge(0);
              loginPw.setMaxAge(0);
              loginId.setPath("/");
              loginPw.setPath("/");
              resp.addCookie(loginId);
              resp.addCookie(loginPw);
          }
        session.removeAttribute("loginUser");// 🍒세션 하나만 삭제 => 로그아웃
        redirectAttributes.addFlashAttribute("msg","로그아웃 되었습니다."); // ; 세미콜론 찍어야 컴파일
        return "redirect:/";
    }


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
            Integer autoLogin, // 로그인시 autoLogin 있으면 쿠키를 만들어서 자동로그인 (쿠키는 response 객체에 저장해야한다.)// 자동로그인에 사용예정 // int 타입의 래퍼클래스인 Integer 는 자료형으로 null 값을 갖는다.
            // int autoLogin, // 🍒input 체크박스를 체크안하면 null 이 넘어가는데, int 는 null 을 파싱할 수 없어서 에러가 발생!
            // => 자동로그인 체크박스 체크시 - ok / 🍒체크 안할 시 int 타입인 경우 페이지 에러가 뜬다. // int 는 null 을 파싱할 수 없어서 Integer 타입으로
            HttpSession session, // 톰캣 로그인 // session : 서버에 저장되는 정보
            RedirectAttributes redirectAttributes, // 성공메세지에 사용
            @SessionAttribute(required = false) String redirectPage, // 파라미터 없으면 null
            HttpServletResponse resp) throws Exception { // 쿠키를 담을 response 객체
        // 🍒2. 파라미터 받는법 getter,setter 가 정의된 Dto(파라미터의 필드가 모두 포함된) 를 받는다. => UserDto user
        // @RequestAttribute 를 하게되면(@RequestAttribute UserDto user) Dto 에 있는 필드를 모두 파라미터로 받아야해서 설정이 더 필요해서 안쓰는 편
        // @RequestAttribute == @RequestParam
        // @RequestParam(required = true) 설정이 기본적으로 포함된 것
        // session 로그인할때 서버에 저장되는 정보 // 톰캣에 session 이 가져와진다.

        // 🍒1. 파라미터 받는법 (매개변수로 파라미터와 똑같은 필드명과 타입을 받는다)
        // @RequestParam(name="uId", required = true) String uId,
        // String pw){ // required=true (기본값) 파라미터가 없으면 400 에러를 띄워라

        // 😋🍒redirect 페이지에 로그인성공, 로그인 실패 메세지를 전달하는 방법
        // 1. 파라미터로  ?msg=로그인 성공"; (권장x)  => 파라미터가 전달된다.(redirectAttributes.addAttribute)
        // => redirectAttributes.addAttribute("msg","로그인성공"); // http://localhost:8080/?msg=%EB%A1%9C%EA%B7%B8%EC%9D%B8%EC%84%B1%EA%B3%B5
        // 2. Session 에 추가한 후에 사용하고 삭제 (권장o - 보안)
        // 3. RedirectAttributes (자동으로 세션에 추가 및 삭제해주는 것)
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
            // 🍉회원가입 이메일 인증 절차
            if(loginUser.getStatus()==UserDto.StatusType.EMAIL_CHECK) {
                // 이메일을 확인할 때까지 로그인 금지
                redirectAttributes.addFlashAttribute("msg","이메일을 확인해야 가입을 완료합니다.");
                redirectAttributes.addAttribute("uId",loginUser.getUId());
                return "redirect:/user/emailCheck.do";
            }

            if(autoLogin!=null && autoLogin==1) { // 자동로그인 값이 있고! 그 값이 1이면! // 1 : 로그인 form 에서 자동로그인 체크시 value
                String encryptIdValue=AESEncryption.encryptValue(loginUser.getUId());
                String encryptPwValue=AESEncryption.encryptValue(loginUser.getPw());
                // ynjFUVAtbk6TWUwKEoGm1A==
                // Wxnal5f0WQOLQAXPArZNyg
                // 대칭키가 서버가 재시작해도 파일을 똑같은 것을 쓰기 때문에 암호가 같다.


                // 로그인 성공하면 쿠키파일 생성
                Cookie loginId=new Cookie("SPRING_LOGIN_ID",encryptIdValue); // key, value
                Cookie loginPw=new Cookie("SPRING_LOGIN_PW",encryptPwValue); // key, value
                loginId.setMaxAge(7*24*60*60); // 쿠키 만료시간 (일주일)
                loginPw.setMaxAge(7*24*60*60);
                loginId.setPath("/"); // /user/** => /**  // 서버최상위 다음의 모든 파일을 유효범위(자동로그인)로 설정
                loginPw.setPath("/");

                resp.addCookie(loginId); // response 에 쿠키를 담는다.
                resp.addCookie(loginPw);
                // 세션유지시간을 저장하기 위해 만든 것? JESSIONID

            }
            redirectAttributes.addFlashAttribute("msg","로그인성공"); // 세션에 저장되기 때문에 파라미터에 보이지 않는다. 이걸 이용해서 모달창으로 띄울 수 있다.! // 세션에 저장되었다가 사용하면 바로 삭제
            // 🍒redirectAttributes.addFlashAttribute : 세션의 일부(세션처럼 넘어간다) (redirect 페이지에서) 사용하고 삭제된다
            session.setAttribute("loginUser",loginUser); // // session 객체에 loginUser 속성을 저장
            if(redirectPage!=null){
                session.removeAttribute("redirectPage");
                return "redirect:"+redirectPage; // 인덱스 페이지
                // redirect : 처리페이지(서버 내에서 페이지 호출) - Get 방식
            }
            return "redirect:/";

        }else{ // 로그인유저가 null 이면 (로그인 실패)
            redirectAttributes.addFlashAttribute("msg","아이디나 패스워드를 확인하세요!"); // 세션에 저장되었다가 사용하면 바로 삭제
            return "redirect:/user/login.do"; // 로그인페이지 호출
        }
    }

    // -- end 로그인


//    @GetMapping("/{uId}/detail.do") // 헐.. ㅜ 경로를 계속 /user/ 를 붙여서 에러가 났음..
//    public String detail(@PathVariable String uId, Model model){
//        UserDto user=null;
//        try{
//            user=userService.detail(uId);
//            model.addAttribute("user", user);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "/user/detail";
//    }








}
