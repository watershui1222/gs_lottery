package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.OpenResultBO;
import com.gs.commons.entity.OpenresultMo6hc;
import com.gs.commons.mapper.OpenresultMo6hcMapper;
import com.gs.commons.service.OpenresultMo6hcService;
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
* @description 针对表【t_openresult_mo6hc】的数据库操作Service实现
* @createDate 2024-04-04 19:54:40
*/
@Service
public class OpenresultMo6hcServiceImpl extends ServiceImpl<OpenresultMo6hcMapper, OpenresultMo6hc>
    implements OpenresultMo6hcService{
    @Autowired
    private OpenresultMo6hcMapper openresultMo6hcMapper;

    @Override
    public int batchOpenResult(List<OpenresultMo6hc> list) {
        return openresultMo6hcMapper.batchOpenResult(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<OpenresultMo6hc> wrapper = new QueryWrapper<OpenresultMo6hc>().lambda();
        Date nowTime = MapUtil.getDate(params, "nowTime");
        wrapper.le(null != nowTime, OpenresultMo6hc::getOpenResultTime, nowTime);
        wrapper.orderByDesc(OpenresultMo6hc::getOpenResultTime);
        IPage<OpenresultMo6hc> page = this.page(
                new Query<OpenresultMo6hc>().getPage(params),
                wrapper);
        List<OpenresultMo6hc> records = page.getRecords();
        List<OpenResultBO> openResultBOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(records)) {
            for (OpenresultMo6hc record : records) {
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




