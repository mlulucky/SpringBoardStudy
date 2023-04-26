package com.acorn.springboardstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources(@PropertySource("classpath:/env.properties")) // 이메일비밀번호를 깃허브에 올릴때 제외해서 올리도록 설정
public class SpringBoardStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoardStudyApplication.class, args);
	}

}
