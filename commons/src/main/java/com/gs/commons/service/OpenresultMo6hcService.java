package com.gs.commons.service;

import com.gs.commons.entity.OpenresultBjpk10;
import com.gs.commons.entity.OpenresultMo6hc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_mo6hc】的数据库操作Service
* @createDate 2024-04-04 19:54:40
*/
public interface OpenresultMo6hcService extends IService<OpenresultMo6hc> {
    int batchOpenResult(List<OpenresultMo6hc> list);
}