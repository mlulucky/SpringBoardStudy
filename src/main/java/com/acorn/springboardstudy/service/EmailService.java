package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


// @Service : @Component 의 자식, 트랜잭션을 정의할때 사용 . 여러 dto 를 실행하고 @Transaction 을 정의할때 사용. 명시적 목적도 있다.
@Component // => db 안하고, 트랜잭션안하니까. 관리만 하니까 컴포넌트 사용
public class EmailService {
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) { // 🍉@AllArgsConstructor 쓰는 이유?
        this.javaMailSender = javaMailSender;
    }
    public void sendMail(EmailDto emailDto) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage(); // 메일을 생성하겠다.
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailDto.getTo()); // 에러가 뜰수있음 => 예외위임처리하기
        mimeMessageHelper.setSubject(emailDto.getTitle());
        mimeMessageHelper.setText(emailDto.getMessage(),true);// true : html 코드를 보내겠다.
        javaMailSender.send(mimeMessage);
    }
}
