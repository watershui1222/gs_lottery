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
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.entity.OpenresultBjpk10;
import com.gs.commons.entity.OpenresultCqssc;
import com.gs.commons.mapper.OpenresultBjpk10Mapper;
import com.gs.commons.service.OpenresultBjpk10Service;
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
* @description 针对表【t_openresult_bjpk10】的数据库操作Service实现
* @createDate 2024-04-04 19:21:52
*/
@Service
public class OpenresultBjpk10ServiceImpl extends ServiceImpl<OpenresultBjpk10Mapper, OpenresultBjpk10>
    implements OpenresultBjpk10Service{
    @Autowired
    private OpenresultBjpk10Mapper openresultBjpk10Mapper;

    @Override
    public int batchOpenResult(List<OpenresultBjpk10> list) {
        return openresultBjpk10Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultBjpk10> wrapper = new QueryWrapper<OpenresultBjpk10>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultBjpk10::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultBjpk10::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultBjpk10::getOpenResultTime);
        IPage<OpenresultBjpk10> page = this.page(
                new Query<OpenresultBjpk10>().getPage(params),
                wrapper);
        List<OpenresultBjpk10> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultBjpk10 record : records) {
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

    @Override
    public OpenresultTimeBO getOneDataByTime(Date currentTime, Date lastTime) {

        LambdaQueryWrapper<OpenresultBjpk10> wrapper = Wrappers.lambdaQuery(OpenresultBjpk10.class)
                .le(null != currentTime, OpenresultBjpk10::getOpenTime, currentTime)
                .ge(null != currentTime, OpenresultBjpk10::getOpenResultTime, currentTime)
                .le(null != lastTime, OpenresultBjpk10::getOpenResultTime, lastTime)
                .orderByDesc(OpenresultBjpk10::getOpenResultTime);


        Page<OpenresultBjpk10> page = this.page(new Page<>(1, 1), wrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            OpenresultTimeBO openresultTimeBO = new OpenresultTimeBO();
            BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), openresultTimeBO);
            return openresultTimeBO;
        }
        return null;
    }
}




