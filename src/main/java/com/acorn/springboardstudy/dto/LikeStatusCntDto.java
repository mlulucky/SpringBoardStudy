package com.acorn.springboardstudy.dto;

import lombok.Data;

// 가상테이블_뷰 란?
// 원래 테이블에는 데이터가 들어있지만 가상테이블 View에는 데이터가 없고 SQL만 저장되어 있다.
// 사용자가 해당 View에 접근하면 그 때 가상테이블 View에 들어있던 SQL이 수행되어 결과를 가져오는 것

//|-----|----|-----|----|
//|like |sad |best |bad |
//|-----|----|-----|----|
//|5    |2   |2    |2   |
//|-----|----|-----|----|
//  SELECT
//  COUNT(CASE WHEN status='LIKE' THEN 1 END) as 'like',
//  COUNT(CASE WHEN status='SAD' THEN 1 END) as 'sad',
//  COUNT(CASE WHEN status='BEST' THEN 1 END) as 'best',
//  COUNT(CASE WHEN status='BAD' THEN 1 END) as 'bad'
//  FROM board_likes
//  WHERE b_id=#{bId}

// board_likes and reply_likes
@Data
public class LikeStatusCntDto {
    // board_likes 테이블의 상태의 결과만을 따로 테이블로 만든것과 같다. (가상테이블_뷰)
    // Status 의 집계(Count) 결과
    private int like;
    private int sad;
    private int bad;
    private int best;
    private  int id; // b_id 또는 br_id
    private String status; // 로그인한 사람이 누른 좋아요 내역


}
