package com.acorn.springboardstudy.mapper;
import com.acorn.springboardstudy.dto.LikeStatusCntDto;
import com.acorn.springboardstudy.dto.ReplyLikeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper // Mybatis 컨테이너에서 관리하는 객체
public interface ReplyLikeMapper {
    // 댓글의 좋아요 싫어요 최고에요 나빠요 개수를 반환
    // 유저가 댓글에 좋아요 싫어요 최고에요 나빠요 수를 반환
    // 댓글에 유저가 좋아요 싫어요 최고에요 나빠요 를 클릭(등록)
    // 댓글(br_id)에서 로그인한 유저(u_id)가 좋아요를 이미 했는지 확인 (했다면 표시되게끔)
    // 댓글에서 유저가 좋아요를 이미 했다면 싫어요 최고에요 나빠요를 클릭했을때 바로 수정
    // 댓글에서 유저가 좋아요를 이미 했다면 좋아요를 취소(삭제)

    ReplyLikeDto findByUidAndBrId(@Param("uId") String uId, @Param("brId") int brId);
    LikeStatusCntDto countStatusByBrId(int brId);
    LikeStatusCntDto countStatusByUId(String uId);
    int insertOne(ReplyLikeDto replyLike);
    int updateOne(ReplyLikeDto replyLike);
    int deleteOne(ReplyLikeDto replyLike);


}
