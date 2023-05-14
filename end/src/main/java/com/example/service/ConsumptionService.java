package com.example.service;

import com.example.entity.Consumption;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ConsumptionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConsumptionService extends ServiceImpl<ConsumptionMapper, Consumption> {

    @Resource
    private ConsumptionMapper consumptionMapper;

}
