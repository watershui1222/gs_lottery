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
import com.gs.commons.mapper.OpenresultFc3dMapper;
import com.gs.commons.service.OpenresultFc3dService;
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
* @description 针对表【t_openresult_fc3d】的数据库操作Service实现
* @createDate 2024-04-04 20:33:52
*/
@Service
public class OpenresultFc3dServiceImpl extends ServiceImpl<OpenresultFc3dMapper, OpenresultFc3d>
    implements OpenresultFc3dService{
    @Autowired
    private OpenresultFc3dMapper openresultFc3dMapper;
    @Override
    public int batchOpenResult(List<OpenresultFc3d> list) {
        return openresultFc3dMapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultFc3d> wrapper = new QueryWrapper<OpenresultFc3d>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultFc3d::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultFc3d::getOpenResultTime);
        IPage<OpenresultFc3d> page = this.page(
                new Query<OpenresultFc3d>().getPage(params),
                wrapper);
        List<OpenresultFc3d> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultFc3d record : records) {
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

        LambdaQueryWrapper<OpenresultFc3d> wrapper = Wrappers.lambdaQuery(OpenresultFc3d.class)
                .le(null != currentTime, OpenresultFc3d::getOpenTime, currentTime)
                .ge(null != currentTime, OpenresultFc3d::getOpenResultTime, currentTime)
                .le(null != lastTime, OpenresultFc3d::getOpenResultTime, lastTime)
                .orderByDesc(OpenresultFc3d::getOpenResultTime);


        Page<OpenresultFc3d> page = this.page(new Page<>(1, 1), wrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            OpenresultTimeBO openresultTimeBO = new OpenresultTimeBO();
            BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), openresultTimeBO);
            return openresultTimeBO;
        }
        return null;
    }
}




