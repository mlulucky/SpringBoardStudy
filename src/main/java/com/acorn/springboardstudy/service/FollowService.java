package com.acorn.springboardstudy.service;

import com.acorn.springboardstudy.dto.FollowDto;

public interface FollowService {
    int remove(FollowDto followDto);
    int register(FollowDto followDto);


}
