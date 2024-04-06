package com.gs.business.client;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.commons.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class LotteryClient {

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    public LotteryCurrQsBO getCurrQs(String lotteryCode) {
        Date now = new Date();
        if (StringUtils.equals(lotteryCode, "JSK3")) {
            LambdaQueryWrapper<OpenresultJsk3> wrapper = new LambdaQueryWrapper<OpenresultJsk3>()
                    .le(OpenresultJsk3::getOpenTime, now)
                    .ge(OpenresultJsk3::getOpenResultTime, now)
                    .orderByDesc(OpenresultJsk3::getOpenResultTime);
            List<OpenresultJsk3> list = openresultJsk3Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }
        return null;
    }
}
