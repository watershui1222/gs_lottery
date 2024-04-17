package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultGs1mfc3d;
import com.gs.commons.mapper.OpenresultGs1mfc3dMapper;
import com.gs.commons.service.OpenresultGs1mfc3dService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import icu.mhb.mybatisplus.plugln.tookit.Lists;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_openresult_gs1mfc3d】的数据库操作Service实现
* @createDate 2024-04-17 17:06:47
*/
@Service
public class OpenresultGs1mfc3dServiceImpl extends ServiceImpl<OpenresultGs1mfc3dMapper, OpenresultGs1mfc3d>
    implements OpenresultGs1mfc3dService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultGs1mfc3d> wrapper = new QueryWrapper<OpenresultGs1mfc3d>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultGs1mfc3d::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultGs1mfc3d::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultGs1mfc3d::getOpenResultTime);
        IPage<OpenresultGs1mfc3d> page = this.page(
                new Query<OpenresultGs1mfc3d>().getPage(params),
                wrapper);
        List<OpenresultGs1mfc3d> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultGs1mfc3d record : records) {
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




