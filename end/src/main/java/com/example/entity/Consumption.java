package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("consumption")
public class Consumption extends Model<Consumption> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 成本名称 
      */
    private String consumeName;

    /**
      * 花费金额 
      */
    private BigDecimal consumeSpend;

    /**
      * 关联预算 
      */
    private String budgetRel;

    /**
      * 关联项目 
      */
    private String projectRel;

}