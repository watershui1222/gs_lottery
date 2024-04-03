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
 * @TableName t_user_login_log
 */
@TableName(value ="t_user_login_log")
@Data
public class UserLoginLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String user_name;

    /**
     * 登录IP
     */
    @TableField(value = "login_ip")
    private String login_ip;

    /**
     * 地址详情
     */
    @TableField(value = "addr_detail")
    private String addr_detail;

    /**
     * 登录域名
     */
    @TableField(value = "login_domain")
    private String login_domain;

    /**
     * 登录时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}