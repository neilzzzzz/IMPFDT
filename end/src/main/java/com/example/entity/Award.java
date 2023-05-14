package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("award")
public class Award extends Model<Award> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 奖罚内容 
      */
    private String awardName;

    /**
      * 奖励,惩罚 
      */
    private String typeRadio;

    /**
      * 待执行,执行完成 
      */
    private String statusRadio;

    /**
      * 奖罚金额 
      */
    private BigDecimal moneycount;

    /**
      * 奖罚人员 
      */
    private String userRel;

}