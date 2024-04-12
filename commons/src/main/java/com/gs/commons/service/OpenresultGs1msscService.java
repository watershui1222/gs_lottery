package com.gs.commons.service;

import com.gs.commons.entity.OpenresultGs1mssc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mssc】的数据库操作Service
* @createDate 2024-04-11 13:05:14
*/
public interface OpenresultGs1msscService extends IService<OpenresultGs1mssc> {
    PageUtils queryPage(Map<String,Object> params);
}
