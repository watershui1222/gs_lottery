package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.entity.OpenresultMo6hc;
import com.gs.commons.utils.PageUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_mo6hc】的数据库操作Service
* @createDate 2024-04-04 19:54:40
*/
public interface OpenresultMo6hcService extends IService<OpenresultMo6hc> {
    int batchOpenResult(List<OpenresultMo6hc> list);
    PageUtils queryPage(Map<String,Object> params);
    OpenresultTimeBO getCurrentQs(Date date);
}
