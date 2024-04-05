package com.gs.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Deposit;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.service.DepositService;
import com.gs.commons.mapper.DepositMapper;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_deposit】的数据库操作Service实现
* @createDate 2024-04-05 15:34:40
*/
@Service
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit>
    implements DepositService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Deposit> wrapper = new QueryWrapper<Deposit>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Deposit::getUserName, userName);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, Deposit::getCreateTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, Deposit::getCreateTime, endTime);

        wrapper.orderByDesc(Deposit::getCreateTime);
        IPage<Deposit> page = this.page(
                new Query<Deposit>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}




