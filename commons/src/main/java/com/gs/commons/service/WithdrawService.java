package com.gs.commons.service;

import com.gs.commons.entity.Withdraw;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author 69000
* @description 针对表【t_withdraw】的数据库操作Service
* @createDate 2024-04-05 17:17:25
*/
public interface WithdrawService extends IService<Withdraw> {

    PageUtils queryPage(Map<String,Object> params);
}
