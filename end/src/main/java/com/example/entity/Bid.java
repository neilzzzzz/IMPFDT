package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("bid")
public class Bid extends Model<Bid> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 投标名称 
      */
    private String bidName;

    /**
      * 投标项目 
      */
    private String biddingRel;

    /**
      * 投标日期 
      */
    private String createTime;

    /**
      * 进行中,已完成,中止,未开始 
      */
    private String bidstatusRadio;

}