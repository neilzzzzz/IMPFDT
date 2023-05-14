package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("scheme")
public class Scheme extends Model<Scheme> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 方案名称 
      */
    private String schemeName;

    /**
      * 所属项目 
      */
    private String projectRel;

    /**
      * 方案附件 
      */
    private String file;

    /**
      * 方案简介 
      */
    private String schemeInfo;

    /**
      * 方案发起人 
      */
    private String userRel;

}