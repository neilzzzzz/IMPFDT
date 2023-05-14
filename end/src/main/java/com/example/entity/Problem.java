package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("problem")
public class Problem extends Model<Problem> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 问题标题 
      */
    private String problemName;

    /**
      * 问题类型 
      */
    private String typeRel;

    /**
      * 一般,紧急,重要 
      */
    private String degreeRadio;

    /**
      * 问题描述 
      */
    private String problemDesc;

    /**
      * 问题图片 
      */
    private String img;

    /**
      * 所属项目 
      */
    private String projectRel;

}