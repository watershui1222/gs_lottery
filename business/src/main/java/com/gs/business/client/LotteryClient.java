package com.gs.business.client;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.commons.entity.*;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.*;
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
    @Autowired
    private OpenresultBjkl8Service openresultBjkl8Service;
    @Autowired
    private OpenresultBjpk10Service openresultBjpk10Service;
    @Autowired
    private OpenresultCqsscService openresultCqsscService;
    @Autowired
    private OpenresultFc3dService openresultFc3dService;
    @Autowired
    private OpenresultFtService openresultFtService;
    @Autowired
    private OpenresultGd11x5Service openresultGd11x5Service;
    @Autowired
    private OpenresultMo6hcService openresultMo6hcService;
    @Autowired
    private OpenresultPcddService openresultPcddService;

    public LotteryCurrQsBO getCurrQs(String lotteryCode) {
        Date now = new Date();
        if (StringUtils.equals(lotteryCode, LotteryCodeEnum.JSK3.getLotteryCode())) {
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
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjpk10> wrapper = new LambdaQueryWrapper<OpenresultBjpk10>()
                    .le(OpenresultBjpk10::getOpenTime, now)
                    .ge(OpenresultBjpk10::getOpenResultTime, now)
                    .orderByDesc(OpenresultBjpk10::getOpenResultTime);
            List<OpenresultBjpk10> list = openresultBjpk10Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.CQSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultCqssc> wrapper = new LambdaQueryWrapper<OpenresultCqssc>()
                    .le(OpenresultCqssc::getOpenTime, now)
                    .ge(OpenresultCqssc::getOpenResultTime, now)
                    .orderByDesc(OpenresultCqssc::getOpenResultTime);
            List<OpenresultCqssc> list = openresultCqsscService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FC3D.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFc3d> wrapper = new LambdaQueryWrapper<OpenresultFc3d>()
                    .le(OpenresultFc3d::getOpenTime, now)
                    .ge(OpenresultFc3d::getOpenResultTime, now)
                    .orderByDesc(OpenresultFc3d::getOpenResultTime);
            List<OpenresultFc3d> list = openresultFc3dService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFt> wrapper = new LambdaQueryWrapper<OpenresultFt>()
                    .le(OpenresultFt::getOpenTime, now)
                    .ge(OpenresultFt::getOpenResultTime, now)
                    .orderByDesc(OpenresultFt::getOpenResultTime);
            List<OpenresultFt> list = openresultFtService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GD11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGd11x5> wrapper = new LambdaQueryWrapper<OpenresultGd11x5>()
                    .le(OpenresultGd11x5::getOpenTime, now)
                    .ge(OpenresultGd11x5::getOpenResultTime, now)
                    .orderByDesc(OpenresultGd11x5::getOpenResultTime);
            List<OpenresultGd11x5> list = openresultGd11x5Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.MO6HC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultMo6hc> wrapper = new LambdaQueryWrapper<OpenresultMo6hc>()
                    .le(OpenresultMo6hc::getOpenTime, now)
                    .ge(OpenresultMo6hc::getOpenResultTime, now)
                    .orderByDesc(OpenresultMo6hc::getOpenResultTime);
            List<OpenresultMo6hc> list = openresultMo6hcService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjkl8> wrapper = new LambdaQueryWrapper<OpenresultBjkl8>()
                    .le(OpenresultBjkl8::getOpenTime, now)
                    .ge(OpenresultBjkl8::getOpenResultTime, now)
                    .orderByDesc(OpenresultBjkl8::getOpenResultTime);
            List<OpenresultBjkl8> list = openresultBjkl8Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.PCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultPcdd> wrapper = new LambdaQueryWrapper<OpenresultPcdd>()
                    .le(OpenresultPcdd::getOpenTime, now)
                    .ge(OpenresultPcdd::getOpenResultTime, now)
                    .orderByDesc(OpenresultPcdd::getOpenResultTime);
            List<OpenresultPcdd> list = openresultPcddService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }
        return null;
    }
}
