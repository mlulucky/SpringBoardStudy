package com.acorn.springboardstudy.dto;

import lombok.Data;

// table reply_likes
@Data
public class ReplyLikeDto {
    // 테이블에 나오는 칼럼명

    // uk(br_id + u_id) : 유저가 해당 댓글에 좋아요를 한번만 하게 하기 위해
    private int rlId;  // pk (Generate Key) (댓글좋아요 아이디)
    private int brId; // fk board.br_id  (보드댓글 아이디)
    private String uId; // fk user.u_id
    private String status; // ['LIKE', 'BAD', 'SAD', 'BEST']
}
