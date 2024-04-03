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
 * @TableName t_user_account
 */
@TableName(value ="t_user_account")
@Data
public class UserAccount implements Serializable {
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
     * 1:银行卡 2:微信 3:支付宝 4:虚拟货币
     */
    @TableField(value = "account_type")
    private Integer accountType;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channelName;

    /**
     * 账号地址
     */
    @TableField(value = "account_no")
    private String accountNo;

    /**
     * 开户网点
     */
    @TableField(value = "address")
    private String address;

    /**
     * 持卡人姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}