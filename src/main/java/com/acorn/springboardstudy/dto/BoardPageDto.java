package com.acorn.springboardstudy.dto;

import lombok.Data;

@Data // getter,setter => @ModelAttribute 를 쓸 수 있다.
public class BoardPageDto { // public - 다른곳에서 import 할 수 있음
    enum BoardsType { // 🔥왜 private 안붙이는가 ?   // sql injection 을 막으려고
        b_id,
        u_id,
        post_time,
        update_time,
        title,
        content,
        view_count
    }
    enum DirectType { // sql injection 을 막으려고
        DESC, ASC
    }


    private int pageNum = 1;
    private int pageSize = 10;

    // order by => order + direct
    private BoardsType order=BoardsType.post_time; // 정렬
//    private String order = "post_time";
    // 필드명을 받아서 실행하는 것은 SQL injection 에 노출된다. (🍒위험)
    // SELECT * FROM board ORDER BY b_id; DROP TABLE board;  (인젝션 노출)
    // SELECT * FROM board ORDER BY "b_id; DROP TABLE board;" (인젝션방지)
    // => 파라미터를 문자열로 표시하면 sql injection 을 방지할 수 있는데 order by 는 필드명을 출력해야 하기 때문에 파라미터로 작성 불가능
    private DirectType direct=DirectType.DESC; // 정렬
//    private String direct = "DESC";

    private BoardsType searchField;
    private String searchValue;
    private String orderBy;

    public String getOrderBy(){
        // SELECT * FROM ~~ ORDER BY b_id DESC
        // SELECT * FROM ~~ ORDER BY b_idDESC
        // SELECT * FROM ~~ ORDER BY null null 안되게 하려고

        if(this.order!=null && this.direct!=null){ // 검색 카테고리와 정렬을 선택 안할시 null 발생 => 에러발생
            return this.order+" "+this.direct;
        }else if(this.order!=null){
            this.direct=DirectType.ASC;
            return this.order+" "+this.direct;
        }
        return BoardsType.update_time+ " "+DirectType.DESC;
    }
}
