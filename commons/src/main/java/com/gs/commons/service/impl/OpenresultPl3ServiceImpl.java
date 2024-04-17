package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultPcdd;
import com.gs.commons.entity.OpenresultPl3;
import com.gs.commons.mapper.OpenresultPl3Mapper;
import com.gs.commons.service.OpenresultPl3Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_pl3】的数据库操作Service实现
* @createDate 2024-04-16 20:00:44
*/
@Service
public class OpenresultPl3ServiceImpl extends ServiceImpl<OpenresultPl3Mapper, OpenresultPl3>
    implements OpenresultPl3Service{

    @Autowired
    private OpenresultPl3Mapper openresultPl3Mapper;
    @Override
    public int batchOpenResult(List<OpenresultPl3> list) {
        return openresultPl3Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultPl3> wrapper = new QueryWrapper<OpenresultPl3>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultPl3::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultPl3::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultPl3::getOpenResultTime);
        IPage<OpenresultPl3> page = this.page(
                new Query<OpenresultPl3>().getPage(params),
                wrapper);
        List<OpenresultPl3> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultPl3 record : records) {
                OpenResultBO openResultBO = new OpenResultBO();
                openResultBO.setQs(record.getQs());
                openResultBO.setOpenResult(record.getOpenResult() == null ? "" : record.getOpenResult());
                openResultBO.setOpenStatus(record.getOpenStatus());
                openResultBO.setOpenResultTime(record.getOpenResultTime());
                openResultBO.setCurrCount(record.getCurrCount());
                openResultBOList.add(openResultBO);
            }
        }
        return new PageUtils(openResultBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




