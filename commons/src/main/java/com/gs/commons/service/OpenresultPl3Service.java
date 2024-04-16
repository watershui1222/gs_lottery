package com.gs.commons.service;

import com.gs.commons.entity.OpenresultPl3;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_pl3】的数据库操作Service
* @createDate 2024-04-16 20:00:44
*/
public interface OpenresultPl3Service extends IService<OpenresultPl3> {
    PageUtils queryPage(Map<String,Object> params);
}
