package com.example.service;

import com.example.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.AttendanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AttendanceService extends ServiceImpl<AttendanceMapper, Attendance> {

    @Resource
    private AttendanceMapper attendanceMapper;

}
