package com.gs.commons.service;

import com.gs.commons.entity.OpenresultCqssc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultJsk3;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_cqssc】的数据库操作Service
* @createDate 2024-04-04 13:19:04
*/
public interface OpenresultCqsscService extends IService<OpenresultCqssc> {
    int batchOpenResult(List<OpenresultCqssc> list);
}