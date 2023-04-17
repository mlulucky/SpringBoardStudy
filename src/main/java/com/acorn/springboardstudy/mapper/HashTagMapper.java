package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.HashTagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // mybatis Mapper 에서 자동으로 등록, 관리.
public interface HashTagMapper {
    // 태그검색(연관 태그 리스트 조회), 태그조회(게시글에 태그 등록 전에 태그가 존재하는지 확인 _ 하나만 나온다) 등록, 삭제(x), 수정(x)
    List<HashTagDto> findByTagContains(String tag); // WHERE tag like '홍%' (홍을 포함하는 전부, % : 컨테인즈) => 홍대 홍대입구 홍대맛집
    HashTagDto findByTag(String tag); // 등록전 태그가 있으면 등록안한다.
    int insertOne(String tag);
}
