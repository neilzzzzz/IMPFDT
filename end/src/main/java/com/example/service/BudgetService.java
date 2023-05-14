package com.example.service;

import com.example.entity.Budget;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.BudgetMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BudgetService extends ServiceImpl<BudgetMapper, Budget> {

    @Resource
    private BudgetMapper budgetMapper;

}
