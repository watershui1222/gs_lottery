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
 * @TableName t_ele_game
 */
@TableName(value ="t_ele_game")
@Data
public class EleGame implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台代码
     */
    @TableField(value = "plat_code")
    private String platCode;

    /**
     * 子平台代码
     */
    @TableField(value = "plat_sub_code")
    private String platSubCode;

    /**
     * 游戏代码
     */
    @TableField(value = "game_code")
    private String gameCode;

    /**
     * 游戏名称
     */
    @TableField(value = "game_name")
    private String gameName;

    /**
     * 游戏图标
     */
    @TableField(value = "img")
    private String img;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 排序号
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}