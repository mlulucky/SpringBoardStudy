package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardHashTagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // mybatis 컨테이너 관리
public interface BoardHashTagMapper {
    // 게시글 + 태그리스 (게시글에 해시태그 리스트 포함하기)
    // 태그 검색 시 해시태그리스트 + 태그 수
    // 태그 등록
    // 태그 삭제 (원본해시태그 삭제 아니고, 게시글에 해시태그만 삭제)
    List<BoardHashTagDto> findByBId(int bId); // bId 로 해시태그 리스트를 조회
    int countByTag(String tag);
    int insertOne(BoardHashTagDto boardHashTagDto);
    int deleteOne(BoardHashTagDto boardHashTagDto);

}
