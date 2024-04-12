package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mpk10;
import com.gs.commons.mapper.OpenresultGs1mpk10Mapper;
import com.gs.commons.service.OpenresultGs1mpk10Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mpk10】的数据库操作Service实现
* @createDate 2024-04-11 13:05:14
*/
@Service
public class OpenresultGs1mpk10ServiceImpl extends ServiceImpl<OpenresultGs1mpk10Mapper, OpenresultGs1mpk10>
    implements OpenresultGs1mpk10Service{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mpk10> wrapper = new QueryWrapper<OpenresultGs1mpk10>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mpk10::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mpk10::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mpk10::getOpenResultTime);
        IPage<OpenresultGs1mpk10> page = this.page(
                new Query<OpenresultGs1mpk10>().getPage(params),
                wrapper);
        List<OpenresultGs1mpk10> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mpk10 record : records) {
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




