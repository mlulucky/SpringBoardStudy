package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.EmailDto;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 테스트
class EmailServiceTest {
    @Autowired
    private EmailService emailService;
    @Test
    void sendMail() throws MessagingException {
        EmailDto emailDto=new EmailDto();
        emailDto.setTo("dmswjdans@gmail.com"); // 본인이 확인할수있는, 로그인할수있는 이메일 계정 // 없는 메일을 써도 오류가 뜨진 않는다.
        emailDto.setTitle("이메일 보내기 테스트");
        emailDto.setMessage("<p>내용 입니다.<b>문은정~~!!</b></p>");
        emailService.sendMail(emailDto); // 예외발생할수있음 => 예외위임처리
    }
}