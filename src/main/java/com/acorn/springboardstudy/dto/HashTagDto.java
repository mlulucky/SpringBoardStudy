package com.acorn.springboardstudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HashTagDto {
    private String tag;

    // 조인! - 해시태그 숫자 받아오기 => fetch 지연로딩으로 불러오기
    @JsonProperty(value="bCnt") // json 파싱을 잘못해서 bcnt 로 파싱한걸 bCnt 로 파싱하게 한다.
    private int bCnt; //  fetch.lazy (지연로딩) SELECT COUNT(*) FROM board_hashtags WHERE tag='홍대'
}
