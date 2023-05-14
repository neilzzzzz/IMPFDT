package com.example.mapper;

import com.example.entity.Progress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ProgressMapper extends BaseMapper<Progress> {

    /**
     * 获取所有进行中项目进度
     * @return
     */
    public List<Progress> getAllProgress();

    /**
     * 查询所有'进行中'项目数量
     * @return
     */
    public Integer getOngoing();

    /**
     * 查询所有进度之和
     * @return
     */
    public Integer getSumProgress();


}
