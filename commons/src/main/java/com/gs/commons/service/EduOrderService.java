package com.gs.commons.service;

import com.gs.commons.entity.EduOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author 69000
* @description 针对表【t_edu_order】的数据库操作Service
* @createDate 2024-04-03 17:29:50
*/
public interface EduOrderService extends IService<EduOrder> {
    PageUtils queryPage(Map<String,Object> params);

}
