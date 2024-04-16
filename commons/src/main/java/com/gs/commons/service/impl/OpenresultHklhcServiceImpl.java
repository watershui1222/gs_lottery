package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultHklhc;
import com.gs.commons.mapper.OpenresultHklhcMapper;
import com.gs.commons.service.OpenresultHklhcService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_hklhc】的数据库操作Service实现
* @createDate 2024-04-16 11:43:39
*/
@Service
public class OpenresultHklhcServiceImpl extends ServiceImpl<OpenresultHklhcMapper, OpenresultHklhc>
    implements OpenresultHklhcService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultHklhc> wrapper = new QueryWrapper<OpenresultHklhc>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultHklhc::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultHklhc::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultHklhc::getOpenResultTime);
        IPage<OpenresultHklhc> page = this.page(
                new Query<OpenresultHklhc>().getPage(params),
                wrapper);
        List<OpenresultHklhc> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultHklhc record : records) {
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




