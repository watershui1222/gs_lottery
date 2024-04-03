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
    private String userName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 余额宝余额
     */
    @TableField(value = "yeb_balance")
    private BigDecimal yebBalance;

    /**
     * 余额宝利息
     */
    @TableField(value = "yeb_interest")
    private BigDecimal yebInterest;

    /**
     * 登录密码
     */
    @TableField(value = "login_pwd")
    private String loginPwd;

    /**
     * 支付密码
     */
    @TableField(value = "pay_pwd")
    private String payPwd;

    /**
     * 状态(0:正常 1:停用 2:密码输入错误上限锁定)
     */
    @TableField(value = "login_status")
    private Integer loginStatus;

    /**
     * 支付状态(0:正常 1:停用)
     */
    @TableField(value = "pay_status")
    private Integer payStatus;

    /**
     * 手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 邀请码
     */
    @TableField(value = "referral_code")
    private String referralCode;

    /**
     * 用户头像ID
     */
    @TableField(value = "avatar_id")
    private Integer avatarId;

    /**
     * 等级ID
     */
    @TableField(value = "level_id")
    private Integer levelId;

    /**
     * 分组ID
     */
    @TableField(value = "group_id")
    private Integer groupId;

    /**
     * 注册时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_time")
    private Date lastTime;

    /**
     * 最后登录IP
     */
    @TableField(value = "last_ip")
    private String lastIp;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 注册IP
     */
    @TableField(value = "register_ip")
    private String registerIp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}