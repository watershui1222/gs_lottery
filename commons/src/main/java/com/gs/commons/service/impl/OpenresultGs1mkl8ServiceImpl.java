package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mkl8;
import com.gs.commons.mapper.OpenresultGs1mkl8Mapper;
import com.gs.commons.service.OpenresultGs1mkl8Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mkl8】的数据库操作Service实现
* @createDate 2024-04-12 17:45:32
*/
@Service
public class OpenresultGs1mkl8ServiceImpl extends ServiceImpl<OpenresultGs1mkl8Mapper, OpenresultGs1mkl8>
    implements OpenresultGs1mkl8Service{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mkl8> wrapper = new QueryWrapper<OpenresultGs1mkl8>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mkl8::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mkl8::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mkl8::getOpenResultTime);
        IPage<OpenresultGs1mkl8> page = this.page(
                new Query<OpenresultGs1mkl8>().getPage(params),
                wrapper);
        List<OpenresultGs1mkl8> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mkl8 record : records) {
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




