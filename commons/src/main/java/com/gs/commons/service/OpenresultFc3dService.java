package com.gs.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultFc3d;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_fc3d】的数据库操作Service
* @createDate 2024-04-04 20:33:52
*/
public interface OpenresultFc3dService extends IService<OpenresultFc3d> {
    int batchOpenResult(List<OpenresultFc3d> list);
}
