package com.gs.commons.service;

import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.entity.OpenresultPcdd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_pcdd】的数据库操作Service
* @createDate 2024-04-04 17:44:12
*/
public interface OpenresultPcddService extends IService<OpenresultPcdd> {
    int batchOpenResult(List<OpenresultPcdd> list);
}
