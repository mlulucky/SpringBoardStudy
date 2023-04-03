package com.acorn.springboardstudy.mapper;

import com.acorn.springboardstudy.dto.BoardReplyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardReplyMapper {
    // 게시글에 댓글 리스트
    // 대댓글 리스트
    // 댓글 수정 폼 출력시 댓글 상세
    // 댓글 등록
    // 댓글 수정
    // 댓글 삭제

    List<BoardReplyDto> findByBIdAndParentBrIdIsNull(int bId); // 게시글 댓글리스트
    // parent_br_id(부모댓글 키) 가 null 이면 - 게시글 댓글 / 아니면 대댓글 (게시글 댓글의 댓글)
    // br_id 댓글 번호 parent_br_id 가 있으면

    List<BoardReplyDto> findByParentBrId(int brId); // 대댓글 리스트
    BoardReplyDto findByBrId(int brId); // 댓글 수정폼 출력시 댓글 상세
    int insertOne(BoardReplyDto boardReply);
    int updateOne(BoardReplyDto boardReply);
    int deleteOne(int brId);

}
