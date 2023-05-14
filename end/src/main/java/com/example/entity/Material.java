package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("material")
public class Material extends Model<Material> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 物资名称 
      */
    private String materialName;

    /**
      * 物品单价 
      */
    private BigDecimal price;

    /**
      * 物品数量 
      */
    private Integer quantity;

    /**
      * 进出货时间 
      */
    private String time;

    /**
      * 负责人 
      */
    private String userRel;

    /**
      * 所属仓库 
      */
    private String depositoryRel;

    /**
      * 办公用材,建筑耗材,装修耗材,其他材料 
      */
    private String typeRadio;

    /**
      * 入库,出库 
      */
    private String statusRadio;

}