package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("attendance")
public class Attendance extends Model<Attendance> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 考勤内容 
      */
    private String attName;

    /**
      * 考勤时间 
      */
    private String attTime;

    /**
      * 考勤人员 
      */
    private String userRel;

}