package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_bjkl8】的数据库操作Service
* @createDate 2024-04-04 16:24:11
*/
public interface OpenresultBjkl8Service extends IService<OpenresultBjkl8> {
    int batchOpenResult(List<OpenresultBjkl8> list);

    PageUtils queryPage(Map<String,Object> params);
}
