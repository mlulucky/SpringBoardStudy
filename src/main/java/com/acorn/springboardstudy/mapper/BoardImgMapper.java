package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardImgDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper // mybatis 컨테이너에서 관리되는 객체를 의미
public interface BoardImgMapper {
    // 게시글에서 조회되는 이미지 리스트
    // 게시글에 이미지 등록
    // 게시글에 이미지 삭제 (수정 x)
    List<BoardImgDto> findByBId(int bId);
    BoardImgDto findByBiId(int biId); // biId (이미지번호)는 pk 라서 무조건 하나의 결과가 나온다.
    int insertOne(BoardImgDto boardImg);
    int deleteOne(int biId);

}
