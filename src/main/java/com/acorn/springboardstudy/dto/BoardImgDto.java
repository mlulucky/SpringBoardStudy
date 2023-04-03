package com.acorn.springboardstudy.dto;

import lombok.Data;

@Data // getter/setter 자동생성
public class BoardImgDto {

    private int biId; // pk (Generate Key)
    private int bId; // fk board.b_id (board 테이블의 b_id 필드를 참조)
    private String imgPath;

//    private int bi_id; // pk (Generate Key)
//    private int b_id; // fk board.b_id
//    private String img_path;


}
