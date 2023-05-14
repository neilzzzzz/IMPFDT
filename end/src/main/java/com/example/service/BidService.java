package com.example.service;

import com.example.entity.Bid;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.BidMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BidService extends ServiceImpl<BidMapper, Bid> {

    @Resource
    private BidMapper bidMapper;

}
