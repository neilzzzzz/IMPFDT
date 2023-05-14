package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("contract")
public class Contract extends Model<Contract> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 合同名称 
      */
    private String contractName;

    /**
      * 内容描述 
      */
    private String content;

    /**
      * 合同文件 
      */
    private String file;

    /**
      * 创建时间 
      */
    private String createTime;

    /**
      * 所属项目 
      */
    private String projectRel;

    /**
      * 有效合同,无效合同,效力待定合同,可撤销合同 
      */
    private String typeRadio;

}