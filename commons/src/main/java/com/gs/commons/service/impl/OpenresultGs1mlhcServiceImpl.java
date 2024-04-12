package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mlhc;
import com.gs.commons.mapper.OpenresultGs1mlhcMapper;
import com.gs.commons.service.OpenresultGs1mlhcService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mlhc】的数据库操作Service实现
* @createDate 2024-04-11 18:58:36
*/
@Service
public class OpenresultGs1mlhcServiceImpl extends ServiceImpl<OpenresultGs1mlhcMapper, OpenresultGs1mlhc>
    implements OpenresultGs1mlhcService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mlhc> wrapper = new QueryWrapper<OpenresultGs1mlhc>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mlhc::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mlhc::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mlhc::getOpenResultTime);
        IPage<OpenresultGs1mlhc> page = this.page(
                new Query<OpenresultGs1mlhc>().getPage(params),
                wrapper);
        List<OpenresultGs1mlhc> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mlhc record : records) {
                OpenResultBO openResultBO = new OpenResultBO();
                openResultBO.setQs(record.getQs());
                openResultBO.setOpenResult(record.getOpenResult());
                openResultBO.setOpenStatus(record.getOpenStatus());
                openResultBO.setOpenResultTime(record.getOpenResultTime());
                openResultBO.setCurrCount(record.getCurrCount());
                openResultBOList.add(openResultBO);
            }
        }
        return new PageUtils(openResultBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




