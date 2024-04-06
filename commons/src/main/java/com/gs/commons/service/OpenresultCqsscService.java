package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.entity.OpenresultCqssc;
import com.gs.commons.utils.PageUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_cqssc】的数据库操作Service
* @createDate 2024-04-04 13:19:04
*/
public interface OpenresultCqsscService extends IService<OpenresultCqssc> {
    int batchOpenResult(List<OpenresultCqssc> list);
    PageUtils queryPage(Map<String,Object> params);
    OpenresultTimeBO getOneDataByTime(Date currentTime, Date lastTime);
}
