package com.example.service;

import com.example.entity.Depository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.DepositoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DepositoryService extends ServiceImpl<DepositoryMapper, Depository> {

    @Resource
    private DepositoryMapper depositoryMapper;

}
