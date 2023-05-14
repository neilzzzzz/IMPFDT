package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("depository")
public class Depository extends Model<Depository> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 仓库名称 
      */
    private String depositoryName;

    /**
      * 仓库地址 
      */
    private String depositoryPlace;

    /**
      * 专业仓库,通用仓库 
      */
    private String typeRadio;

    /**
      * 仓库说明 
      */
    private String depositoryDesc;

}