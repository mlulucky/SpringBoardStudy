package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardImgDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImpTest {
    @Autowired
    private BoardService boardService;
    @Test
    void imgList() {
        List<BoardImgDto> boardImgDtos = boardService.imgList(new int[]{3, 4, 5});// 이미지번호 3,4,5 리스트
        System.out.println("boardImgDtos = " + boardImgDtos);
        // boardImgDtos = [BoardImgDto(biId=3, bId=2, imgPath=/img/board/3.jpg), BoardImgDto(biId=4, bId=2, imgPath=/img/board/4.jpg), BoardImgDto(biId=5, bId=3, imgPath=/img/board/5.jpg)]
        Assertions.assertNotNull(boardImgDtos);
    }
}