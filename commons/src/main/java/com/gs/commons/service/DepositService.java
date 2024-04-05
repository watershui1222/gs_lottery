package com.gs.commons.service;

import com.gs.commons.entity.Deposit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author 69000
* @description 针对表【t_deposit】的数据库操作Service
* @createDate 2024-04-05 15:34:40
*/
public interface DepositService extends IService<Deposit> {

    PageUtils queryPage(Map<String,Object> params);

}
