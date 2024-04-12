package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mssc;
import com.gs.commons.mapper.OpenresultGs1msscMapper;
import com.gs.commons.service.OpenresultGs1msscService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mssc】的数据库操作Service实现
* @createDate 2024-04-11 13:05:14
*/
@Service
public class OpenresultGs1msscServiceImpl extends ServiceImpl<OpenresultGs1msscMapper, OpenresultGs1mssc>
    implements OpenresultGs1msscService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mssc> wrapper = new QueryWrapper<OpenresultGs1mssc>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mssc::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mssc::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mssc::getOpenResultTime);
        IPage<OpenresultGs1mssc> page = this.page(
                new Query<OpenresultGs1mssc>().getPage(params),
                wrapper);
        List<OpenresultGs1mssc> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mssc record : records) {
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




