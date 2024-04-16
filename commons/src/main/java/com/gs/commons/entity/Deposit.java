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
 * @TableName t_deposit
 */
@TableName(value ="t_deposit")
@Data
public class Deposit implements Serializable {
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
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 充值金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 1:银行卡 2:微信 3:支付宝 4:虚拟货币 5:在线支付 6:后台充值
     */
    @TableField(value = "deposit_type")
    private Integer depositType;

    /**
     * 发起时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 审核时间
     */
    @TableField(value = "check_time")
    private Date checkTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 操作人
     */
    @TableField(value = "oper_name")
    private String operName;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 充值账户详情
     */
    @TableField(value = "account_detail")
    private String accountDetail;

    /**
     * 用户充值信息详情
     */
    @TableField(value = "deposit_detail")
    private String depositDetail;

    /**
     * 0:待审核 1:审核成功 2:审核失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 用户IP
     */
    @TableField(value = "user_ip")
    private String userIp;

    /**
     * IP详情
     */
    @TableField(value = "ip_detail")
    private String ipDetail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}