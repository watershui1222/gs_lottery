package com.gs.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_user_info
 */
@TableName(value ="t_user_info")
@Data
public class UserInfo implements Serializable {
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
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nick_name;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String real_name;

    /**
     * 余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 余额宝余额
     */
    @TableField(value = "yeb_balance")
    private BigDecimal yeb_balance;

    /**
     * 余额宝利息
     */
    @TableField(value = "yeb_interest")
    private BigDecimal yeb_interest;

    /**
     * 登录密码
     */
    @TableField(value = "login_pwd")
    private String login_pwd;

    /**
     * 支付密码
     */
    @TableField(value = "pay_pwd")
    private String pay_pwd;

    /**
     * 状态(0:正常 1:停用 2:密码输入错误上限锁定)
     */
    @TableField(value = "login_status")
    private Integer login_status;

    /**
     * 支付状态(0:正常 1:停用)
     */
    @TableField(value = "pay_status")
    private Integer pay_status;

    /**
     * 手机号
     */
    @TableField(value = "user_phone")
    private String user_phone;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String user_agent;

    /**
     * 邀请码
     */
    @TableField(value = "referral_code")
    private String referral_code;

    /**
     * 用户头像ID
     */
    @TableField(value = "avatar_id")
    private Integer avatar_id;

    /**
     * 等级ID
     */
    @TableField(value = "level_id")
    private Integer level_id;

    /**
     * 分组ID
     */
    @TableField(value = "group_id")
    private Integer group_id;

    /**
     * 注册时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_time")
    private Date last_time;

    /**
     * 最后登录IP
     */
    @TableField(value = "last_ip")
    private String last_ip;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}