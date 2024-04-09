package com.gs.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_banner
 */
@TableName(value ="t_banner")
@Data
public class Banner implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * banner图
     */
    @TableField(value = "img")
    private String img;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 排序号
     */
    @TableField(value = "pxh")
    private Integer pxh;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 操作人
     */
    @TableField(value = "oper_name")
    private String operName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}