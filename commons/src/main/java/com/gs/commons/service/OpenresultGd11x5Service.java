package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.entity.OpenresultGd11x5;
import com.gs.commons.utils.PageUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gd11x5】的数据库操作Service
* @createDate 2024-04-04 15:56:40
*/
public interface OpenresultGd11x5Service extends IService<OpenresultGd11x5> {
    int batchOpenResult(List<OpenresultGd11x5> list);
    PageUtils queryPage(Map<String,Object> params);
    OpenresultTimeBO getOneDataByTime(Date currentTime, Date lastTime);
}
