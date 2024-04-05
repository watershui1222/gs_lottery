package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultBjpk10;
import com.gs.commons.mapper.OpenresultBjpk10Mapper;
import com.gs.commons.service.OpenresultBjpk10Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_bjpk10】的数据库操作Service实现
* @createDate 2024-04-04 19:21:52
*/
@Service
public class OpenresultBjpk10ServiceImpl extends ServiceImpl<OpenresultBjpk10Mapper, OpenresultBjpk10>
    implements OpenresultBjpk10Service{
    @Autowired
    private OpenresultBjpk10Mapper openresultBjpk10Mapper;

    @Override
    public int batchOpenResult(List<OpenresultBjpk10> list) {
        return openresultBjpk10Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultBjpk10> wrapper = new QueryWrapper<OpenresultBjpk10>().lambda();

        wrapper.orderByDesc(OpenresultBjpk10::getOpenResultTime);

        IPage<OpenresultBjpk10> page = this.page(
                new Query<OpenresultBjpk10>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}




