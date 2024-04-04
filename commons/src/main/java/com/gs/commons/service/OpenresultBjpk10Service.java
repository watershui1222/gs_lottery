package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultBjpk10;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_bjpk10】的数据库操作Service
* @createDate 2024-04-04 19:21:52
*/
public interface OpenresultBjpk10Service extends IService<OpenresultBjpk10> {
    int batchOpenResult(List<OpenresultBjpk10> list);
}
