package com.acorn.springboardstudy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties({"handler"}) // => 프록시 객체에 있는 handler 를 제외해라
// => 왜 제외해야하나?
// 핸들러는 직렬화가 안되서 반환을 못한다.
// 핸들러를 제외하고 아래에 Dto 에 정의된 get/set 만 dto 로 반환하겠다.

// Table board_replies
public class BoardReplyDto {
    private int brId; // pk generate key(==Auto increment) 일반적인 키 // NOT NULL
    @JsonProperty("bId")// 대문자가 한글자 앞에 있어서 json 이 대소문자 인식을 못해서
    private int bId; // fk boards.b_id // NOT NULL
    @JsonProperty("uId")
    private String uId; // fk users.u_id // NOT NULL
    private Integer parentBrId; // 기본형은 null 이 될수 없다 => Integer 자료형으로 바꾸기(Null 사용가능)  ISNULL(Null 허용) // 나 자신을 참조 // self join fk board_replies.br_id
    // NULL 이 될 수 있는 경우에만 주의! (NOT NULL - NULL 이 될수없는 경우는 NULL 처리 할 필요 없다.)
    // private int parentBrId; // 기본형은 null 인경우 0으로 출력(조회)된다. // 나 자신을 참조 // self join fk board_replies.br_id
    // parentBrId 가 없는 경우(대댓글x, 댓글) 테이블에서는 값이 없으면 null 이 되는데, 실제 쿼리를 실행하면 0 으로 파싱(형변환)이 된다.
    // 이유는 ? 기본형(int) 은 null 이 될수 없다. 값이 없으면 0을 갖는다.
    // 해결 => int 기본형 => Integer 래퍼클래스 (기본형을 객체로 다루기 위해 사용하는 클래스)

    private Date postTime; // CURRENT_TIMESTAMP
    private Date updateTime; // CURRENT_TIMESTAMP on update
    private String status; // enum [PUBLIC,PRIVATE,REPORT,BLOCK]
    private String imgPath;
    private String content;
    private LikeStatusCntDto likes; // board_replies : reply_likes = 1 : N (게시글 하나에 좋아요 여러개) (그렇지만 집계한 결과만 가져옴)
    private List<BoardReplyDto> replies; // 대댓글 (리스트로 나 자신의 타입 BoardReplyDto 을 참조 (셀프조인))
    // board_replies : board_replies = 1 : N (self join) - 내가 나를 참조. 또 내가 나를 참조. 또 내가 나를 참조.... 무한반복...

}

//        디비 테이블에서 값이 없으면 0 대신 null 처리되는 이유는 다음과 같습니다.
//        Null은 값이 없음을 명시적으로 표현합니다.
//        Null은 데이터베이스에서 값이 존재하지 않는 상태를 나타내는 특별한 값입니다. 값이 없음을 명시적으로 표현하여 데이터베이스에서 누락된 정보를 추적하고 관리할 수 있습니다.
//
//        0으로 처리할 경우 오류 가능성이 존재합니다.
//        값이 없는 상태를 0으로 처리하면, 값이 실제로 0일 때와 구분하기 어려울 수 있습니다. 예를 들어, 어떤 제품의 판매량이 0인 경우와 아직 판매가 시작되지 않아서 판매량이 없는 경우를 구분하기 위해서는 Null 값을 사용해야 합니다.
//
//        자원을 절약할 수 있습니다.
//        0을 사용하면, 모든 레코드에 대해 기본값으로 0을 할당해야 합니다. 그러나 Null을 사용하면, 레코드의 크기를 줄일 수 있습니다.
//
//        SQL 표준에 따른 권장사항입니다.
//        SQL 표준에서는 값이 없음을 나타내는 특별한 값으로 Null을 권장합니다.
//
//        따라서, 값이 없는 경우 Null 값을 사용하면, 데이터베이스의 일관성과 정확성을 유지할 수 있으며, 값이 없음을 명확하게 표현할 수 있습니다.