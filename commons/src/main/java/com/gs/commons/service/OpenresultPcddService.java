package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.entity.OpenresultPcdd;
import com.gs.commons.utils.PageUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_pcdd】的数据库操作Service
* @createDate 2024-04-04 17:44:12
*/
public interface OpenresultPcddService extends IService<OpenresultPcdd> {
    int batchOpenResult(List<OpenresultPcdd> list);
    PageUtils queryPage(Map<String,Object> params);
    OpenresultTimeBO getCurrentQs(Date date);
}
