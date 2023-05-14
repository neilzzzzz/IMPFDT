package com.example.mapper;

import com.example.entity.Consumption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ConsumptionMapper extends BaseMapper<Consumption> {

    /**
     * 统计所有成本
     * @return
     */
    public Double sumCsm();

    /**
     * 获取一个项目所有的成本
     * @param projectname
     * @return
     */
    public Double getOneCsmSum(String projectname);

    /**
     * 通过预算名称查找项目名
     * @param budgetname
     * @return
     */
    public String getNameByRel(String budgetname);
}
