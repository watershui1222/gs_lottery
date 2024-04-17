package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mpl3;
import com.gs.commons.mapper.OpenresultGs1mpl3Mapper;
import com.gs.commons.service.OpenresultGs1mpl3Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mpl3】的数据库操作Service实现
* @createDate 2024-04-17 17:06:47
*/
@Service
public class OpenresultGs1mpl3ServiceImpl extends ServiceImpl<OpenresultGs1mpl3Mapper, OpenresultGs1mpl3>
    implements OpenresultGs1mpl3Service{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mpl3> wrapper = new QueryWrapper<OpenresultGs1mpl3>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mpl3::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mpl3::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mpl3::getOpenResultTime);
        IPage<OpenresultGs1mpl3> page = this.page(
                new Query<OpenresultGs1mpl3>().getPage(params),
                wrapper);
        List<OpenresultGs1mpl3> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mpl3 record : records) {
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




