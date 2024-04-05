package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.mapper.OpenresultBjkl8Mapper;
import com.gs.commons.service.OpenresultBjkl8Service;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_bjkl8】的数据库操作Service实现
* @createDate 2024-04-04 16:24:11
*/
@Service
public class OpenresultBjkl8ServiceImpl extends ServiceImpl<OpenresultBjkl8Mapper, OpenresultBjkl8>
    implements OpenresultBjkl8Service{
    @Autowired
    private OpenresultBjkl8Mapper openresultBjkl8Mapper;

    @Override
    public int batchOpenResult(List<OpenresultBjkl8> list) {
        return openresultBjkl8Mapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultBjkl8> wrapper = new QueryWrapper<OpenresultBjkl8>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultBjkl8::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultBjkl8::getOpenResultTime);
        IPage<OpenresultBjkl8> page = this.page(
                new Query<OpenresultBjkl8>().getPage(params),
                wrapper);
        List<OpenresultBjkl8> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultBjkl8 record : records) {
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
}




