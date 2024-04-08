package com.gs.business.pojo;


import lombok.Data;

@Data
public class PlatLoginUrlBO {

    private String userName;

    /**
     * 平台代码
     */
    private String platCode;

    private String platSubCode;

    private String gameCode;


    /**
     * 平台游戏账号
     */
    private String platUserName;

    /**
     * 平台游戏密码
     */
    private String platUserPassword;

    /**
     * 1:pc 2:手机
     */
    private int device = 2;
}
