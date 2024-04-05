package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultCqssc;
import com.gs.commons.mapper.OpenresultCqsscMapper;
import com.gs.commons.service.OpenresultCqsscService;
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
* @description 针对表【t_openresult_cqssc】的数据库操作Service实现
* @createDate 2024-04-04 13:19:04
*/
@Service
public class OpenresultCqsscServiceImpl extends ServiceImpl<OpenresultCqsscMapper, OpenresultCqssc>
    implements OpenresultCqsscService{

    @Autowired
    private OpenresultCqsscMapper openresultCqsscMapper;

    @Override
    public int batchOpenResult(List<OpenresultCqssc> list) {
        return openresultCqsscMapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultCqssc> wrapper = new QueryWrapper<OpenresultCqssc>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultCqssc::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultCqssc::getOpenResultTime);
        IPage<OpenresultCqssc> page = this.page(
                new Query<OpenresultCqssc>().getPage(params),
                wrapper);
        List<OpenresultCqssc> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultCqssc record : records) {
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




