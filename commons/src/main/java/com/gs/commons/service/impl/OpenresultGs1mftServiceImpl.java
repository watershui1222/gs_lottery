package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mft;
import com.gs.commons.mapper.OpenresultGs1mftMapper;
import com.gs.commons.service.OpenresultGs1mftService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mft】的数据库操作Service实现
* @createDate 2024-04-11 18:19:21
*/
@Service
public class OpenresultGs1mftServiceImpl extends ServiceImpl<OpenresultGs1mftMapper, OpenresultGs1mft>
    implements OpenresultGs1mftService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mft> wrapper = new QueryWrapper<OpenresultGs1mft>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mft::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mft::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mft::getOpenResultTime);
        IPage<OpenresultGs1mft> page = this.page(
                new Query<OpenresultGs1mft>().getPage(params),
                wrapper);
        List<OpenresultGs1mft> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mft record : records) {
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




