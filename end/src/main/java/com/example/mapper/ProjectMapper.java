package com.example.mapper;

import com.example.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查看进行中项目
     * @return
     */
    public List<Project> onProjectEntity();

    /**
     * 统计项目数量
     * @return
     */
    public Integer findCnt();

    /**
     * 新增项目重名验证
     * @param pname
     * @return 返回0表示非重复，反之重复
     */
    public Integer checkName(String pname);

    /**
     * 查询进行中项目
     * @return
     */
    public Integer onProject();


    /**
     * 修改页重名验证
     * @param oldname
     * @param newname
     * @return返回0表示非重复，反之重复
     */
    public Integer checkUpdate(String oldname,String newname);

}
