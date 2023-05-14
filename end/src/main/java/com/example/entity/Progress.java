package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("progress")
public class Progress extends Model<Progress> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 进度名称 
      */
    private String progressName;

    /**
      * 进度内容 
      */
    private String content;

    /**
      * 所属项目 
      */
    private String projectRel;

    /**
      * 现场图片 
      */
    private String img;

    /**
      * 完成时间 
      */
    private String finishTime;

    /**
      * 完成比例 
      */
    private Integer proportion;

}