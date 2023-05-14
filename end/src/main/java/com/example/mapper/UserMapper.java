package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 统计用户数量
     * @return
     */
    public Integer findAllUser();


}
