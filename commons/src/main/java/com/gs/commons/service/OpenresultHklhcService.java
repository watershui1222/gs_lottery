package com.gs.commons.service;

import com.gs.commons.entity.OpenresultHklhc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
 * @author tommm
 * @description 针对表【t_openresult_hklhc】的数据库操作Service
 * @createDate 2024-04-16 11:43:39
 */
public interface OpenresultHklhcService extends IService<OpenresultHklhc> {
    PageUtils queryPage(Map<String,Object> params);

}
