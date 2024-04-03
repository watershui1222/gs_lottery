package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.SysParam;
import com.gs.commons.service.SysParamService;
import com.gs.commons.mapper.SysParamMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_sys_param】的数据库操作Service实现
* @createDate 2024-04-03 17:30:18
*/
@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam>
    implements SysParamService{

    @Override
    public Map<String, String> getAllParamByMap() {
        List<SysParam> list = list();
        return CollStreamUtil.toMap(list, SysParam::getParamKey, SysParam::getParamValue);
    }

}




