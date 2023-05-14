package com.example.mapper;

import com.example.entity.Budget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BudgetMapper extends BaseMapper<Budget> {

    /**
     * 统计总预算
     * @return
     */
    public Double sumBgt();

    /**
     * 获取一个项目的所有预算
     * @param projectname
     * @return
     */
    public Double getOneBgtSum(String projectname);

}
