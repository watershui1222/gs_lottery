package com.gs.commons.service;

import com.gs.commons.entity.OpenresultGs1mlhc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mlhc】的数据库操作Service
* @createDate 2024-04-11 18:58:36
*/
public interface OpenresultGs1mlhcService extends IService<OpenresultGs1mlhc> {
    PageUtils queryPage(Map<String,Object> params);
}
