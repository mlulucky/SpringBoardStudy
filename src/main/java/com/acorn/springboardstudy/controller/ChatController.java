package com.acorn.springboardstudy.controller;

import com.acorn.springboardstudy.dto.ChatMsgDto;
import com.acorn.springboardstudy.dto.ChatRoomDto;
import com.acorn.springboardstudy.dto.UserDto;
import com.acorn.springboardstudy.service.ChatMsgService;
import com.acorn.springboardstudy.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private ChatRoomService chatRoomService;
    private ChatMsgService chatMsgService;
    // mvc 구현
    // 채팅룸 리스트      /chat/room/list.do
    // 채팅룸 상세(입장)   /chat/room/{crId}/detail.do

    // ajax 구현(실시간)
    // 채팅룸에서 메세지 보내기(등록)  /chat/msg/{crId}/register.do
    // 채팅룸에서 메세지 조회  /chat/msg/{crId}/list.do
    // 채팅룸에서 메세지 특정시간 이후 조회  /chat/msg/{crId}/list.do?postTime=2023-04-25+17:04:00

    @GetMapping("/room/list.do")
    public String list(Model model){
        List<ChatRoomDto> rooms=chatRoomService.list();
        model.addAttribute("rooms",rooms);
        return "/chat/roomList";
    }

    @GetMapping("/room/{crId}/detail.do")
    public String detail(Model model, @PathVariable int crId){
        model.addAttribute("crId", crId); // 파라미터를 뷰(html) 에 보냄
        return "/chat/roomDetail";
    }

    @GetMapping("/msg/{crId}/list.do")
    public @ResponseBody List<ChatMsgDto> list( // 파라미터로 받아온 dto 를 json 으로 반환하는게 @ResponseBody
            @PathVariable int crId,
            @RequestParam(required = false) String postTime
    ){
        List<ChatMsgDto> list=null;
        if(postTime==null){
            list=chatMsgService.list(crId);
        }else {
            list=chatMsgService.list(crId,postTime);
        }
        return list;
    }

    @PostMapping("/msg/register.do")
    public @ResponseBody int register(
            @ModelAttribute ChatMsgDto chatMsgDto, // 맵핑
            @SessionAttribute UserDto loginUser){ // 기본적으로 required=true 가 되어있음
        int register=0;
        register=chatMsgService.register(chatMsgDto);
        return  register;
    }





}
