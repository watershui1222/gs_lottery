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
import com.gs.commons.entity.OpenresultFc3d;
import com.gs.commons.entity.OpenresultFt;
import com.gs.commons.mapper.OpenresultFtMapper;
import com.gs.commons.service.OpenresultFtService;
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
* @description 针对表【t_openresult_ft】的数据库操作Service实现
* @createDate 2024-04-04 15:32:12
*/
@Service
public class OpenresultFtServiceImpl extends ServiceImpl<OpenresultFtMapper, OpenresultFt>
    implements OpenresultFtService{
    @Autowired
    private OpenresultFtMapper openresultFtMapper;
    @Override
    public int batchOpenResult(List<OpenresultFt> list) {
        return openresultFtMapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultFt> wrapper = new QueryWrapper<OpenresultFt>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(null != startTime, OpenresultFt::getOpenResultTime, startTime);
        wrapper.le(null != nowTime, OpenresultFt::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultFt::getOpenResultTime);
        IPage<OpenresultFt> page = this.page(
                new Query<OpenresultFt>().getPage(params),
                wrapper);
        List<OpenresultFt> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultFt record : records) {
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

        LambdaQueryWrapper<OpenresultFt> wrapper = Wrappers.lambdaQuery(OpenresultFt.class)
                .le(null != currentTime, OpenresultFt::getOpenTime, currentTime)
                .ge(null != currentTime, OpenresultFt::getOpenResultTime, currentTime)
                .le(null != lastTime, OpenresultFt::getOpenResultTime, lastTime)
                .orderByDesc(OpenresultFt::getOpenResultTime);


        Page<OpenresultFt> page = this.page(new Page<>(1, 1), wrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            OpenresultTimeBO openresultTimeBO = new OpenresultTimeBO();
            BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), openresultTimeBO);
            return openresultTimeBO;
        }
        return null;
    }
}




