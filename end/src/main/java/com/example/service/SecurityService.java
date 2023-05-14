package com.example.service;

import com.example.entity.Security;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SecurityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SecurityService extends ServiceImpl<SecurityMapper, Security> {

    @Resource
    private SecurityMapper securityMapper;

}
