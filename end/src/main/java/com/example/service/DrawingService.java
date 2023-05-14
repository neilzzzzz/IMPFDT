package com.example.service;

import com.example.entity.Drawing;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.DrawingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DrawingService extends ServiceImpl<DrawingMapper, Drawing> {

    @Resource
    private DrawingMapper drawingMapper;

}
