package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultJsk3;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_openresult_jsk3】的数据库操作Service
* @createDate 2024-04-03 17:30:12
*/
public interface OpenresultJsk3Service extends IService<OpenresultJsk3> {

    int insertBatchOrUpdate(List<OpenresultJsk3> list);

    int batchOpenResult(List<OpenresultJsk3> list);
}
