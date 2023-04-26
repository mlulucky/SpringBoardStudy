package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.FollowDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // ajax 만 반환하는 컨트롤러
@RequestMapping("/follow")
@AllArgsConstructor
public class FollowController {
    // 결과를 0,1 만 보내는거라서 jsonIgnore 안해도 된다?
    private FollowService followService;
    @PostMapping("/{uId}/{follower}/handler.do") // http://localhost:8080/follow/user01/handler.do
    public int registerHandler(
            @PathVariable String uId,
            @PathVariable boolean follower,
            @SessionAttribute UserDto loginUser){ // 로그인한 유저만 팔로우 할수있다. 로그인유저 안하면 400 에러
        int register=0;
        // 글쓴이가 자기 자신을 팔로잉 할 수 없다.
        if(loginUser.getUId().equals(uId)) return register;
        FollowDto followDto=new FollowDto();
        if(follower){ // 팔로워 등록 (follower 가 있으면)
            followDto.setToId(loginUser.getUId());
            followDto.setFromId(uId);
        }else{ // 팔로우 등록
            followDto.setToId(uId); // uId == to_id
            followDto.setFromId(loginUser.getUId());
        }
        register=followService.register(followDto); // ajax 는 500 에러는 실패 (이미 팔로우한 사람 or 다시 시도)
        return register;
    }

    // ?follower=true => follower 지우기. false 면 following 지우기
    @DeleteMapping("/{uId}/{follower}/handler.do")
    public int removeHandler(
            @PathVariable String uId,
            @PathVariable boolean follower,
            @SessionAttribute UserDto loginUser){
        int remove=0;
        FollowDto followDto=new FollowDto();
        if(follower){ // 팔로워가 있으면, 팔로워 삭제
            // 나를 구독하는 사람
            followDto.setFromId(uId);
            followDto.setToId(loginUser.getUId());
        }else { // 팔로잉 삭제
            // 내가 구독하는 사람
            followDto.setFromId(loginUser.getUId());
            followDto.setToId(uId);
        }
        remove= followService.remove(followDto);
        return remove;

    }


}
