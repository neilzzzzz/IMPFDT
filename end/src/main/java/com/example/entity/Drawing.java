package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("drawing")
public class Drawing extends Model<Drawing> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 图纸名称 
      */
    private String drawingName;

    /**
      * 附件 
      */
    private String file;

    /**
      * 是,否 
      */
    private String isuueRadio;

    /**
      * 启用,废止 
      */
    private String statusRadio;

    /**
      * 所属项目 
      */
    private String projectRel;

}