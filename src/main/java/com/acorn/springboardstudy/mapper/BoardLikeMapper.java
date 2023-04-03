package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardLikeDto;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper // Mybatis 컨테이너에서 관리하는 객체

public interface BoardLikeMapper {
    // 게시글의 좋아요 싫어요 최고에요 나빠요 개수를 반환
    // 유저가 게시글에 작성한 좋아요 싫어요 최고에요 나빠요 를 반환
    // 게시글에서 유저가 좋아요 싫어요 최고에요 나빠요 를 클릭(등록)
    // 게시글에 로그인한 유저가 좋아요를 했는지 확인
    // 게시글에서 유저가 좋아요를 이미 했다면 싫어요 최고에요 나빠요를 클릭했을때 바로 수정
    // 게시글에서 유저가 좋아요를 이미 했다면 좋아요를 취소(삭제)

//    BoardLikeDto findByBIdAndUId(int bId, String uId); // 테스트할때 파라미터를 못찾아서 @Param 추가를 하니 테스트 통과됨
    BoardLikeDto findByBIdAndUId(@Param("bId") int bId, @Param("uId") String uId); // 테스트할때 파라미터를 못찾아서 @Param 추가를 하니 테스트 통과됨
    LikeStatusCntDto countStatusByBId(int bId); // 게시글번호 필요 => 매개변수// LikeStatusCntDto 타입을 반환.
    LikeStatusCntDto countStatusByUId(String uId);
    int insertOne(BoardLikeDto boardLike);
    int updateOne(BoardLikeDto boardLike);
    int deleteOne(BoardLikeDto boardLikeDto);


}
