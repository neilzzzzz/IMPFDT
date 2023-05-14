package com.example.service;

import com.example.entity.Contract;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ContractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContractService extends ServiceImpl<ContractMapper, Contract> {

    @Resource
    private ContractMapper contractMapper;

}
