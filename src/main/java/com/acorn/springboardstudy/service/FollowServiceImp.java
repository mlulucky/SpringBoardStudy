package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.FollowDto;
import com.acorn.springboardstudy.mapper.FollowMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 스프링에서 의존성 주입이 되는 컨포넌트
@AllArgsConstructor // 의존성 주입 Mapper
public class FollowServiceImp implements FollowService {
    private FollowMapper followMapper;
    @Override
    public int remove(FollowDto followDto) {
        int remove=followMapper.deleteByFromIdAndToId(followDto);
        return remove;
    }

    @Override
    public int register(FollowDto followDto) {
        int register=followMapper.insertOne(followDto);
        return register;
    }


}
