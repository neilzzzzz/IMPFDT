package com.example.service;

import com.example.entity.Dailyreport;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.DailyreportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DailyreportService extends ServiceImpl<DailyreportMapper, Dailyreport> {

    @Resource
    private DailyreportMapper dailyreportMapper;

}
