package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("dailyreport")
public class Dailyreport extends Model<Dailyreport> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 日报标题 
      */
    private String dailyName;

    /**
      * 日报内容 
      */
    private String dailyContent;

    /**
      * 所属项目 
      */
    private String projectRel;

    /**
      * 现场情况 
      */
    private String img;

    /**
      * 填写日期 
      */
    private String createDate;

    /**
      * 填写人 
      */
    private String userRel;

}