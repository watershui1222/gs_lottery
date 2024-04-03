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
 * @TableName t_user_plat
 */
@TableName(value ="t_user_plat")
@Data
public class UserPlat implements Serializable {
    /**
     * id
     */
    @TableField(value = "id")
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String user_name;

    /**
     * 平台代码
     */
    @TableField(value = "plat_code")
    private String plat_code;

    /**
     * 平台游戏账号
     */
    @TableField(value = "plat_user_name")
    private String plat_user_name;

    /**
     * 0:正常 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}