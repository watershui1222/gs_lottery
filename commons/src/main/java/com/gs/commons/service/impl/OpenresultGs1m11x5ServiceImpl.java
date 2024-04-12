package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1m11x5;
import com.gs.commons.mapper.OpenresultGs1m11x5Mapper;
import com.gs.commons.service.OpenresultGs1m11x5Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1m11x5】的数据库操作Service实现
* @createDate 2024-04-12 17:45:32
*/
@Service
public class OpenresultGs1m11x5ServiceImpl extends ServiceImpl<OpenresultGs1m11x5Mapper, OpenresultGs1m11x5>
    implements OpenresultGs1m11x5Service{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1m11x5> wrapper = new QueryWrapper<OpenresultGs1m11x5>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1m11x5::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1m11x5::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1m11x5::getOpenResultTime);
        IPage<OpenresultGs1m11x5> page = this.page(
                new Query<OpenresultGs1m11x5>().getPage(params), wrapper);

        List<OpenresultGs1m11x5> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1m11x5 record : records) {
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




