package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("budget")
public class Budget extends Model<Budget> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 预算名称 
      */
    private String budgetName;

    /**
      * 预算金额 
      */
    private BigDecimal bgtamount;

    /**
      * 预算项目 
      */
    private String projectRel;

    /**
      * 创建时间 
      */
    private String createTime;

}