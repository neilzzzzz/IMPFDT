package com.example.service;

import com.example.entity.Scheme;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SchemeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SchemeService extends ServiceImpl<SchemeMapper, Scheme> {

    @Resource
    private SchemeMapper schemeMapper;

}
