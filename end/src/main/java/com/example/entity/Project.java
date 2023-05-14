package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("project")
public class Project extends Model<Project> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 项目名称 
      */
    private String projectName;

    /**
      * 创建时间 
      */
    private String createTime;

    /**
      * 完成日期 
      */
    private String finishDate;

    /**
      * 项目负责人 
      */
    private String userRel;

    /**
      * 未开始,进行中,项目中止,完成 
      */
    private String statusRadio;

    /**
      * 项目预览 
      */
    private String img;

}