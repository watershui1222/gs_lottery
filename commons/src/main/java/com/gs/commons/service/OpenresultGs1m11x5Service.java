package com.gs.commons.service;

import com.gs.commons.entity.OpenresultGs1m11x5;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1m11x5】的数据库操作Service
* @createDate 2024-04-12 17:45:32
*/
public interface OpenresultGs1m11x5Service extends IService<OpenresultGs1m11x5> {
    PageUtils queryPage(Map<String,Object> params);
}
