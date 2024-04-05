package com.gs.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.mapper.EduOrderMapper;
import com.gs.commons.service.EduOrderService;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_edu_order】的数据库操作Service实现
* @createDate 2024-04-03 17:29:50
*/
@Service
public class EduOrderServiceImpl extends ServiceImpl<EduOrderMapper, EduOrder>
    implements EduOrderService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<EduOrder> wrapper = new QueryWrapper<EduOrder>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), EduOrder::getUserName, userName);

        String platCode = MapUtil.getStr(params, "platCode");
        wrapper.eq(StringUtils.isNotBlank(platCode), EduOrder::getPlatCode, platCode);


        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, EduOrder::getCreateTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, EduOrder::getCreateTime, endTime);

        wrapper.orderByDesc(EduOrder::getCreateTime);
        IPage<EduOrder> page = this.page(
                new Query<EduOrder>().getPage(params),
                wrapper);

        return new PageUtils(page);
    }
}




