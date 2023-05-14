package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("security")
public class Security extends Model<Security> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 检查性质名称 
      */
    private String securityName;

    /**
      * 检查人 
      */
    private String userRel;

    /**
      * 说明 
      */
    private String probDesc;

}