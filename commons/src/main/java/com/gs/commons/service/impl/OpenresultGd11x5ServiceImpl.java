package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.entity.OpenresultGd11x5;
import com.gs.commons.mapper.OpenresultGd11x5Mapper;
import com.gs.commons.service.OpenresultGd11x5Service;
import com.gs.commons.utils.BeanUtil;
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
* @description 针对表【t_openresult_gd11x5】的数据库操作Service实现
* @createDate 2024-04-04 15:56:40
*/
@Service
public class OpenresultGd11x5ServiceImpl extends ServiceImpl<OpenresultGd11x5Mapper, OpenresultGd11x5>
    implements OpenresultGd11x5Service{

    @Autowired
    private OpenresultGd11x5Mapper openresultGd11x5Mapper;

    @Override
    public int batchOpenResult(List<OpenresultGd11x5> list) {
        return openresultGd11x5Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGd11x5> wrapper = new QueryWrapper<OpenresultGd11x5>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultGd11x5::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGd11x5::getOpenResultTime);
        IPage<OpenresultGd11x5> page = this.page(
                new Query<OpenresultGd11x5>().getPage(params),
                wrapper);
        List<OpenresultGd11x5> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGd11x5 record : records) {
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

    @Override
    public OpenresultTimeBO getOneDataByTime(Date currentTime, Date lastTime) {

        LambdaQueryWrapper<OpenresultGd11x5> wrapper = Wrappers.lambdaQuery(OpenresultGd11x5.class)
                .le(null != currentTime, OpenresultGd11x5::getOpenTime, currentTime)
                .ge(null != currentTime, OpenresultGd11x5::getOpenResultTime, currentTime)
                .le(null != lastTime, OpenresultGd11x5::getOpenResultTime, lastTime)
                .orderByDesc(OpenresultGd11x5::getOpenResultTime);


        Page<OpenresultGd11x5> page = this.page(new Page<>(1, 1), wrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            OpenresultTimeBO openresultTimeBO = new OpenresultTimeBO();
            BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), openresultTimeBO);
            return openresultTimeBO;
        }
        return null;
    }
}




