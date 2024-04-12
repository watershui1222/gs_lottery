package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mpcdd;
import com.gs.commons.mapper.OpenresultGs1mpcddMapper;
import com.gs.commons.service.OpenresultGs1mpcddService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mpcdd】的数据库操作Service实现
* @createDate 2024-04-12 17:45:32
*/
@Service
public class OpenresultGs1mpcddServiceImpl extends ServiceImpl<OpenresultGs1mpcddMapper, OpenresultGs1mpcdd>
    implements OpenresultGs1mpcddService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mpcdd> wrapper = new QueryWrapper<OpenresultGs1mpcdd>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mpcdd::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mpcdd::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mpcdd::getOpenResultTime);
        IPage<OpenresultGs1mpcdd> page = this.page(
                new Query<OpenresultGs1mpcdd>().getPage(params),
                wrapper);
        List<OpenresultGs1mpcdd> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mpcdd record : records) {
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




