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
import com.gs.commons.entity.OpenresultMo6hc;
import com.gs.commons.entity.OpenresultPcdd;
import com.gs.commons.mapper.OpenresultPcddMapper;
import com.gs.commons.service.OpenresultPcddService;
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
* @description 针对表【t_openresult_pcdd】的数据库操作Service实现
* @createDate 2024-04-04 17:44:12
*/
@Service
public class OpenresultPcddServiceImpl extends ServiceImpl<OpenresultPcddMapper, OpenresultPcdd>
    implements OpenresultPcddService{
    @Autowired
    private OpenresultPcddMapper openresultPcddMapper;

    @Override
    public int batchOpenResult(List<OpenresultPcdd> list) {
        return openresultPcddMapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultPcdd> wrapper = new QueryWrapper<OpenresultPcdd>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultPcdd::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultPcdd::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultPcdd::getOpenResultTime);
        IPage<OpenresultPcdd> page = this.page(
                new Query<OpenresultPcdd>().getPage(params),
                wrapper);
        List<OpenresultPcdd> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultPcdd record : records) {
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

    @Override
    public OpenresultTimeBO getOneDataByTime(Date currentTime, Date lastTime) {

        LambdaQueryWrapper<OpenresultPcdd> wrapper = Wrappers.lambdaQuery(OpenresultPcdd.class)
                .le(null != currentTime, OpenresultPcdd::getOpenTime, currentTime)
                .ge(null != currentTime, OpenresultPcdd::getOpenResultTime, currentTime)
                .le(null != lastTime, OpenresultPcdd::getOpenResultTime, lastTime)
                .orderByDesc(OpenresultPcdd::getOpenResultTime);


        Page<OpenresultPcdd> page = this.page(new Page<>(1, 1), wrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            OpenresultTimeBO openresultTimeBO = new OpenresultTimeBO();
            BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), openresultTimeBO);
            return openresultTimeBO;
        }
        return null;
    }
}




