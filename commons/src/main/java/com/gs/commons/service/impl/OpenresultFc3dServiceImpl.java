package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultFc3d;
import com.gs.commons.mapper.OpenresultFc3dMapper;
import com.gs.commons.service.OpenresultFc3dService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_fc3d】的数据库操作Service实现
* @createDate 2024-04-04 20:33:52
*/
@Service
public class OpenresultFc3dServiceImpl extends ServiceImpl<OpenresultFc3dMapper, OpenresultFc3d>
    implements OpenresultFc3dService{
    @Autowired
    private OpenresultFc3dMapper openresultFc3dMapper;
    @Override
    public int batchOpenResult(List<OpenresultFc3d> list) {
        return openresultFc3dMapper.batchOpenResult(list);
    }
}




