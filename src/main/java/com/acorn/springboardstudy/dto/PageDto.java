package com.acorn.springboardstudy.dto;

import lombok.Data;

@Data
public class PageDto {
    private int page=1; // 페이지
    private int offset=10; // 한페이지에 몇개를 보여줄거냐
    private int startIndex=0; // 기본값이 0이라서 안해도 되는데, 0으로 설정 // 몇번 인덱스부터 시작할건지
    private String order="post_time"; // 정렬 // 기본값
    private String direct="DESC"; // 정렬순서
    private String search; // 어떻게 서칭할지
    private String searchValue;
    private String searchField;

    // 페이지 네비게이션의 수 알기
    private int totalRows;
    private int lastPage; // == lastPage
    private int prevPage;
    private int nextPage;
    private boolean prev; // 첫번째 페이징버튼에서는 버튼 비활성화 위해
    private boolean next; // 마지막 페이징버튼에서는 버튼 비활성화 위해

    // totalRows 를 셋팅할때 모두 셋팅되게 하기
    public void setTotalRows(int totalRows){ // totalRows 를 셋하면 아래 필드들이 정의가 된다.
        this.totalRows=totalRows;
        // 13/5 = 2
        // => 3
        this.lastPage=(int)Math.ceil((double)totalRows/offset); // 3.0 => int 3
        this.prevPage=this.page-1;
        this.nextPage=this.page+1;
        this.prev=(this.page>1); //  1번 페이지에서는 prev 버튼 비활성화
        this.next=(this.page<lastPage);
    }

    public PageDto(){} // 🍉 기본 생성자 - 파라미터의 required=true 강제를 없애준다.


    public void setSearchValue(String searchValue) {
        // 공백이 아니면
        if(!searchValue.trim().equals("")) this.searchValue = searchValue;
//        if(!searchValue.trim().length()>0) this.searchValue = searchValue;
    }

    public void setSearchField(String searchField) {
        // 공백이 아니면
        if(!searchField.trim().equals(""))this.searchField = searchField;
    }

    public int getStartIndex() {
        this.startIndex=(page-1)*offset;
        return this.startIndex;
//        return (this.startIndex=(1-page)*offset);
    }

    // 🍒생성자를 정의하면 강제하는 것
    // default 생성자 없이 생성자를 정의하고 @ModelAttribute 로 사용하면 생성자에서 사용하는 기본형 파라미터(생성자의 매개변수)들을 required=true 로 정의한다.
    // 정수는 required=true 처리되고 무조건 있어야 한다. (문자열은 null 로 취급이 되서 문자열은 안적어도된다)
    // startIndex 는 페이지를 얻을때 받으려고 해서 아래에 매개변수에 추가 안함. => 1페이지의 첫번째 인덱스는 0, 2페이지의 첫번째 인덱스는 5 ....
//    public PageDto(int page, int offset, String order, String direct, String search) {
//        this.page = page;
//        this.offset = offset;
//        this.order = order;
//        this.direct = direct;
//        this.search = search;
//    }

}
