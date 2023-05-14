package com.example.service;

import com.example.entity.Problem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProblemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProblemService extends ServiceImpl<ProblemMapper, Problem> {

    @Resource
    private ProblemMapper problemMapper;

}
