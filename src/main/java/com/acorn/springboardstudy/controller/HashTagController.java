package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.HashTagDto;
import com.acorn.springboardstudy.service.HashTagService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @Controller : 무조건 view(템플렛엔진) 를 렌더하거나 or redirect 하는 동적 페이지 // @ResponseBody 어노테이션으로 json 응답할 수 있다.
// @RestController : 무조건 json or text 을 응답하는 동적 페이지 (== @ResponseBody)
@RestController // AJAX 를 호출하는 페이지를 구현할때 사용 // 🍉 HTML 을 렌더하지 않는 컨트롤러
@RequestMapping("/hashtag")
@AllArgsConstructor
public class HashTagController {
    private HashTagService hashTagService;
    @GetMapping("/{tag}/search.do") // http://localhost:8080/hashtag/홍대/search.do (http://localhost:8080/hashtag/%ED%99%8D%EB%8C%80/search.do)
    public List<HashTagDto> search(@PathVariable String tag){
        List<HashTagDto> tags=hashTagService.search(tag);
        return tags; // json 으로 응답
        // [{"tag":"홍대","bcnt":8},{"tag":"홍대놀이터","bcnt":1},{"tag":"홍대맛집","bcnt":2},{"tag":"홍대애견","bcnt":0},{"tag":"홍대입구","bcnt":0},{"tag":"홍대카페","bcnt":0}]
        // => [{"tag":"홍대","bCnt":8},{"tag":"홍대놀이터","bCnt":1},{"tag":"홍대맛집","bCnt":2},{"tag":"홍대애견","bCnt":0},{"tag":"홍대입구","bCnt":0},{"tag":"홍대카페","bCnt":0}]
        // => @JsonProperty(value="bCnt") // json 파싱을 잘못해서 bcnt 로 파싱한걸 bCnt 로 파싱하게 한다. (HashTagDto 에서 설정준다.)
    }
}
