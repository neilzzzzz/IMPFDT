package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("feedback")
public class Feedback extends Model<Feedback> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 反馈标题 
      */
    private String feedbackName;

    /**
      * 反馈内容 
      */
    private String feedbackContent;

    /**
      * 反馈时间 
      */
    private String createTime;

    /**
      * 反馈者 
      */
    private String userRel;

    /**
      * 系统反馈,财务反馈,施工反馈,其他反馈 
      */
    private String typeRadio;

}