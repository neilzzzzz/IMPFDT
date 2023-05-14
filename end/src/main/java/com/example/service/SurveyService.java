package com.example.service;

import com.example.entity.Survey;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SurveyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SurveyService extends ServiceImpl<SurveyMapper, Survey> {

    @Resource
    private SurveyMapper surveyMapper;

}
