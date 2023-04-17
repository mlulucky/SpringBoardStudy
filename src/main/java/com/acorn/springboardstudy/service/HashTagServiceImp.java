package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.BoardHashTagDto;
import com.acorn.springboardstudy.dto.HashTagDto;
import com.acorn.springboardstudy.mapper.BoardHashTagMapper;
import com.acorn.springboardstudy.mapper.HashTagMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //
@AllArgsConstructor // pojo 방식으로 생성자를 만들겠다.
public class HashTagServiceImp implements HashTagService{
    private HashTagMapper hashTagMapper;
    private BoardHashTagMapper boardHashTagMapper;


    @Override
    public List<HashTagDto> search(String tag) {
        List<HashTagDto> search=hashTagMapper.findByTagContains(tag);
        return search;
    }

    @Override
    public int register(BoardHashTagDto boardHashTagDto) {
        return 0;
    }

    @Override
    public int remove(BoardHashTagDto boardHashTagDto) {
        return 0;
    }
}
