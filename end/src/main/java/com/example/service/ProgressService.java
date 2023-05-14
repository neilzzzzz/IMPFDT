package com.example.service;

import com.example.entity.Progress;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProgressMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProgressService extends ServiceImpl<ProgressMapper, Progress> {

    @Resource
    private ProgressMapper progressMapper;

}
