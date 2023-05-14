package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("survey")
public class Survey extends Model<Survey> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 报告名称 
      */
    private String surveyName;

    /**
      * 报告内容 
      */
    private String surveyContent;

    /**
      * 调研人 
      */
    private String userRel;

    /**
      * 调研时间 
      */
    private String surveyTime;

}