package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("bidding")
public class Bidding extends Model<Bidding> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 招标名称 
      */
    private String biddingName;

    /**
      * 所属项目 
      */
    private String projectRel;

    /**
      * 发布时间 
      */
    private String createTime;

    /**
      * 进行中,已完成,中止,未开始 
      */
    private String biddingRadio;

    /**
      * 招标人 
      */
    private String userRel;

    /**
      * 招标文件 
      */
    private String file;

}