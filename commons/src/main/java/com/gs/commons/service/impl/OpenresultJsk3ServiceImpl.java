package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.mapper.OpenresultJsk3Mapper;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_openresult_jsk3】的数据库操作Service实现
* @createDate 2024-04-03 17:30:12
*/
@Service
public class OpenresultJsk3ServiceImpl extends ServiceImpl<OpenresultJsk3Mapper, OpenresultJsk3>
    implements OpenresultJsk3Service{

    @Autowired
    private OpenresultJsk3Mapper openresultJsk3Mapper;
    @Override
    public int insertBatchOrUpdate(List<OpenresultJsk3> list) {
        return openresultJsk3Mapper.insertBatchOrUpdate(list);
    }

    @Override
    public int batchOpenResult(List<OpenresultJsk3> list) {
        return openresultJsk3Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultJsk3> wrapper = new QueryWrapper<OpenresultJsk3>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultJsk3::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultJsk3::getOpenResultTime);
        IPage<OpenresultJsk3> page = this.page(
                new Query<OpenresultJsk3>().getPage(params),
                wrapper);
        List<OpenresultJsk3> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultJsk3 record : records) {
                OpenResultBO openResultBO = new OpenResultBO();
                openResultBO.setQs(record.getQs());
                openResultBO.setOpenResult(record.getOpenResult());
                openResultBO.setOpenStatus(record.getOpenStatus());
                openResultBO.setOpenResultTime(record.getOpenResultTime());
                openResultBOList.add(openResultBO);
            }
        }
        return new PageUtils(openResultBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




