package com.example.service;

import com.example.entity.Securitys;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SecuritysMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SecuritysService extends ServiceImpl<SecuritysMapper, Securitys> {

    @Resource
    private SecuritysMapper securitysMapper;

}
