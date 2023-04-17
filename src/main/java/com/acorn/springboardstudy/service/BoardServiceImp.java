package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardDto;
import com.acorn.springboardstudy.dto.BoardImgDto;
import com.acorn.springboardstudy.mapper.BoardImgMapper;
import com.acorn.springboardstudy.mapper.BoardMapper;
import com.acorn.springboardstudy.mapper.UserMapper;
import com.github.pagehelper.PageHelper;

import com.acorn.springboardstudy.dto.*;
import com.acorn.springboardstudy.mapper.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service // @Component 의 일부
@AllArgsConstructor // 의존성 주입 + 생성자 생성
public class BoardServiceImp implements BoardService {
    private BoardMapper boardMapper; // @AllArgsConstructor
    private UserMapper userMapper;
    private BoardImgMapper boardImgMapper; // 🍉보드에서 이미지 등록하기(보드 등록시)
    private BoardHashTagMapper boardHashTagMapper;
    private HashTagMapper hashTagMapper; // 해시태그가 없으면 등록

//    @Value("${static.path}")
//    private String staticPath; // 정적 리소스 위치 (이미지 파일 삭제하기위해)

    // 생성자로 의존성 주입(DI)
//    public BoardServiceImp(BoardMapper boardMapper, UserMapper userMapper, BoardImgMapper boardImgMapper) {
//        this.boardMapper = boardMapper;
//        this.userMapper = userMapper;
//        this.boardImgMapper = boardImgMapper;
//    }



    @Override
    public List<BoardImgDto> imgList(int[] biId) {
        List<BoardImgDto> imgList=null;
        if(biId!=null){
            imgList=new ArrayList<>();
            for(int id : biId) {
                BoardImgDto imgDto=boardImgMapper.findByBiId(id);
                imgList.add(imgDto);
            }
        }
        return imgList; // => mapper 에서 in 사용한 반복문으로 불러오는 방법도 있다.
    }


    @Override
    public List<BoardDto> tagList(String tag, UserDto loginUser) {
        if(loginUser!=null) userMapper.setLoginUserId(loginUser.getUId()); // 로그인한 사람
        List<BoardDto> tagList=boardMapper.findByTag(tag);
        if(loginUser!=null)userMapper.setLoginUserIdNull();
        return tagList;
    }

    // 로그인 한 사람이 없을때!
    @Override
    public List<BoardDto> list(UserDto loginUser, PageDto pageDto) {
        // List<BoardDto> list=boardMapper.findAll(loginUserId); // 서브쿼리로 좋아요 불러오기
        if(loginUser!=null) userMapper.setLoginUserId(loginUser.getUId()); // 로그인한 유저 아이디를 mysql 서버에 변수로 등록
        List<BoardDto> list=this.boardMapper.findAll(pageDto); // 불러올때 페이징도 불러오기 // 지연로딩으로 좋아요 불러오기
        int totalRows=boardMapper.countAll(pageDto);
        pageDto.setTotalRows(totalRows);
        if(loginUser!=null)  userMapper.setLoginUserIdNull(); // 사용이 끝나서 삭제 // 지연로딩으로 조인이 조금 늦어서 불러오기전에 먼저 삭제를 해서(null 을 만듬) 상태가 자꾸 null 이 됨.
        // 보드에서 조회할때는 즉시로딩으로 바꾸기
        return list;
    }

    // 로그인 한 사람이 있을때~!
/*    @Override
    public List<BoardDto> list(String loginUserId) {
//        List<BoardDto> list=boardMapper.findAll(loginUserId); // 서브쿼리로 좋아요 불러오기
        userMapper.setLoginUserId(loginUserId); // 로그인한 유저 아이디를 mysql 서버에 변수로 등록
//        PageHelper.startPage(1,5,"b_id");
        PageHelper.startPage(2,5,"b_id");

        List<BoardDto> list=this.boardMapper.findAll(); // 지연로딩으로 좋아요 불러오기
        userMapper.setLoginUserIdNull(); // 사용이 끝나서 삭제 // 지연로딩으로 조인이 조금 늦어서 불러오기전에 먼저 삭제를 해서(null 을 만듬) 상태가 자꾸 null 이 됨.
        // 보드에서 조회할때는 즉시로딩으로 바꾸기
        return list;
    }*/


    @Override
    @Transactional // AOP(관점지향언어)의 일종(관심사 분리). 커밋과 롤백을 따로 // 시작지점으로 커밋을 하고 예외발생시 커밋지점으로 다시 롤백
    public BoardDto detail(int bId) {
        // 시작지점(dataSource.getConnection().commit() )을 커밋으로 하고 그 이후를 자동으로 try,catch

        // 예외처리 부분
        boardMapper.updateIncrementViewCountBId(bId); // 상세글보기 시 조회수 올리기
        BoardDto detail=boardMapper.findByBId(bId);

        // 예외 발생시 - 커밋지점으로 롤백 : dataSource.getConnection().rollBack()
        return detail;
    }

    @Override
    @Transactional // 보드 등록시 이미지 등록 -> 실패시 롤백하려고~! 🍋중간에 값이 하나라도 잘못되면 등록이 취소가 된다.
    public int register(BoardDto board, List<String> tags) {
        // 🍉bId 가 처음에 null
        int register=0;
        // 🍉insert 할때 bId 가 생성되고 그 값을 마이바티스가 파라미터인 board 에 전달
        register=boardMapper.insertOne(board); // 게시글 등록
        if(board.getImgs()!=null){ // img 가 null 이 아니면
            for(BoardImgDto img : board.getImgs()){ // 🍉이미지에 게시글 번호를 알수없다 => 이미지에 게시글 번호 정해주기!
                img.setBId(board.getBId()); // 🍉생성된 bId 를 이미지에 저장
                register+=boardImgMapper.insertOne(img); // 이미지 등록
            }
        }
        // 태그등록
        if(tags!=null){
            for(String tag:tags){
                HashTagDto hashTag=hashTagMapper.findByTag(tag);
                if(hashTag==null) hashTagMapper.insertOne(tag); // 해시태그가 없으면 등록 => 실패되면 다 취소 => 왜? 트랜잭션 걸어놔서
                BoardHashTagDto boardHashTag=new BoardHashTagDto();
                boardHashTag.setBId(board.getBId()); // 게시글의 번호로 저장을 한다.
                boardHashTag.setTag(tag); // 태그 저장
                boardHashTagMapper.insertOne(boardHashTag); // 등록
            }
        }
            return register;
    }

    @Override
    @Transactional // 이중에 하나라도 문제가 되서 오류가 뜨면 작업을 취소한다. => modify 0 이 반환된다.
    public int modify(BoardDto board, int[] delImgIds, List<String> tags, List<String> delTags) {
        int modify=boardMapper.updateOne(board);
        if(delImgIds!=null){
            for(int biId : delImgIds){
                // 삭제하기 전에 이미지아이디로 이미지 조회해서 이미지 삭제하기
//                BoardImgDto img=boardImgMapper.findByBiId(biId); // 이미지 파일 삭제하기 위해 파일을 조회하기~!
                modify+=boardImgMapper.deleteOne(biId); // 이미지파일 db 에서 지우기
//                File imgFile=new File(staticPath+img.getImgPath()); // 파일 찾기
//                if(imgFile.exists()) imgFile.delete(); // 이미지가 있으면 파일을 삭제 (컴퓨터 저장경로에서 삭제)
            }
        }
        if(tags!=null){
            for(String tag:tags){
                HashTagDto hashTag=hashTagMapper.findByTag(tag);
                if(hashTag==null) modify+=hashTagMapper.insertOne(tag); // 해시태그가 없으면 등록 => 실패되면 다 취소 => 왜? 트랜잭션 걸어놔서
                BoardHashTagDto boardHashTag=new BoardHashTagDto();
                boardHashTag.setBId(board.getBId()); // 게시글의 번호로 저장을 한다.
                boardHashTag.setTag(tag); // 태그 저장
                modify+=boardHashTagMapper.insertOne(boardHashTag); // 등록
            }
        }
        if(delTags!=null){
            for(String tag:delTags){
                BoardHashTagDto boardHashTag=new BoardHashTagDto();
                boardHashTag.setTag(tag);
                boardHashTag.setBId(board.getBId());
                modify+=boardHashTagMapper.deleteOne(boardHashTag);
            }
        }
        return modify;
    }

    @Override
    public int remove(int bId) {
        int remove=boardMapper.deleteOne(bId);
        return remove;
    }

}
