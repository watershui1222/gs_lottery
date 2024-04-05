package com.gs.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Deposit;
import com.gs.commons.entity.Withdraw;
import com.gs.commons.service.WithdrawService;
import com.gs.commons.mapper.WithdrawMapper;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_withdraw】的数据库操作Service实现
* @createDate 2024-04-05 17:17:25
*/
@Service
public class WithdrawServiceImpl extends ServiceImpl<WithdrawMapper, Withdraw>
    implements WithdrawService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<Withdraw> wrapper = new QueryWrapper<Withdraw>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), Withdraw::getUserName, userName);

        wrapper.orderByDesc(Withdraw::getCreateTime);
        IPage<Withdraw> page = this.page(
                new Query<Withdraw>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}




