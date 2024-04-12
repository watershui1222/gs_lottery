package com.gs.business.client;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.business.utils.lottery.*;
import com.gs.commons.entity.*;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.*;
import com.gs.commons.utils.BeanUtil;
import com.gs.commons.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private OpenresultGs1mftService openresultGs1mftService;
    @Autowired
    private OpenresultGs1mk3Service openresultGs1mk3Service;
    @Autowired
    private OpenresultGs1mlhcService openresultGs1mlhcService;
    @Autowired
    private OpenresultGs1mpk10Service openresultGs1mpk10Service;
    @Autowired
    private OpenresultGs1msscService openresultGs1msscService;
    @Autowired
    private OpenresultGs1mkl8Service openresultGs1mkl8Service;
    @Autowired
    private OpenresultGs1m11x5Service openresultGs1m11x5Service;
    @Autowired
    private OpenresultGs1mpcddService openresultGs1mpcddService;

    /**
     * 校验订单输赢
     * @param order
     */
    public void checkWin(LotteryOrder order) {
        if (order.getLotteryType().intValue() == 1) {
            K3Util.checkWin(order);
        } else if (order.getLotteryType().intValue() == 5) {
            PCDDUtil.checkWin(order);
        } else if (order.getLotteryType().intValue() == 3) {
            PK10Util.checkWin(order);
        } else if (order.getLotteryType().intValue() == 2) {
            CQSSCUtil.checkWin(order);
        } else if (order.getLotteryType().intValue() == 8) {
            BJKL8Util.checkWin(order);
        } else if (order.getLotteryType().intValue() == 6) {
            SYX5Util.checkWin(order);
        } else if (order.getLotteryType().intValue() == 4) {
            LHCUtil.checkWin(order);
        }
    }

    /**
     * 根据彩种代码获取当前期
     * @param lotteryCode
     * @return
     */
    public LotteryCurrQsBO getCurrQs(String lotteryCode) {
        Date now = new Date();
        if (StringUtils.equals(lotteryCode, LotteryCodeEnum.JSK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultJsk3> wrapper = new LambdaQueryWrapper<OpenresultJsk3>()
                    .le(OpenresultJsk3::getOpenTime, now)
                    .gt(OpenresultJsk3::getOpenResultTime, now)
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
                    .gt(OpenresultBjpk10::getOpenResultTime, now)
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
                    .gt(OpenresultCqssc::getOpenResultTime, now)
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
                    .gt(OpenresultFc3d::getOpenResultTime, now)
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
                    .gt(OpenresultFt::getOpenResultTime, now)
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
                    .gt(OpenresultGd11x5::getOpenResultTime, now)
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
                    .gt(OpenresultMo6hc::getOpenResultTime, now)
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
                    .gt(OpenresultBjkl8::getOpenResultTime, now)
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
                    .gt(OpenresultPcdd::getOpenResultTime, now)
                    .orderByDesc(OpenresultPcdd::getOpenResultTime);
            List<OpenresultPcdd> list = openresultPcddService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MFT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mft> wrapper = new LambdaQueryWrapper<OpenresultGs1mft>()
                    .le(OpenresultGs1mft::getOpenTime, now)
                    .gt(OpenresultGs1mft::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mft::getOpenResultTime);
            List<OpenresultGs1mft> list = openresultGs1mftService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mk3> wrapper = new LambdaQueryWrapper<OpenresultGs1mk3>()
                    .le(OpenresultGs1mk3::getOpenTime, now)
                    .gt(OpenresultGs1mk3::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mk3::getOpenResultTime);
            List<OpenresultGs1mk3> list = openresultGs1mk3Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MLHC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mlhc> wrapper = new LambdaQueryWrapper<OpenresultGs1mlhc>()
                    .le(OpenresultGs1mlhc::getOpenTime, now)
                    .gt(OpenresultGs1mlhc::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mlhc::getOpenResultTime);
            List<OpenresultGs1mlhc> list = openresultGs1mlhcService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpk10> wrapper = new LambdaQueryWrapper<OpenresultGs1mpk10>()
                    .le(OpenresultGs1mpk10::getOpenTime, now)
                    .gt(OpenresultGs1mpk10::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mpk10::getOpenResultTime);
            List<OpenresultGs1mpk10> list = openresultGs1mpk10Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mssc> wrapper = new LambdaQueryWrapper<OpenresultGs1mssc>()
                    .le(OpenresultGs1mssc::getOpenTime, now)
                    .gt(OpenresultGs1mssc::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mssc::getOpenResultTime);
            List<OpenresultGs1mssc> list = openresultGs1msscService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mkl8> wrapper = new LambdaQueryWrapper<OpenresultGs1mkl8>()
                    .le(OpenresultGs1mkl8::getOpenTime, now)
                    .gt(OpenresultGs1mkl8::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mkl8::getOpenResultTime);
            List<OpenresultGs1mkl8> list = openresultGs1mkl8Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1M11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1m11x5> wrapper = new LambdaQueryWrapper<OpenresultGs1m11x5>()
                    .le(OpenresultGs1m11x5::getOpenTime, now)
                    .gt(OpenresultGs1m11x5::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1m11x5::getOpenResultTime);
            List<OpenresultGs1m11x5> list = openresultGs1m11x5Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpcdd> wrapper = new LambdaQueryWrapper<OpenresultGs1mpcdd>()
                    .le(OpenresultGs1mpcdd::getOpenTime, now)
                    .gt(OpenresultGs1mpcdd::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mpcdd::getOpenResultTime);
            List<OpenresultGs1mpcdd> list = openresultGs1mpcddService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }
        return null;
    }

    /**
     * 获取上一期
     * @param lotteryCode
     * @return
     */
    public LotteryCurrQsBO getLastQs(String lotteryCode) {
        Date now = new Date();
        if (StringUtils.equals(lotteryCode, LotteryCodeEnum.JSK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultJsk3> wrapper = new LambdaQueryWrapper<OpenresultJsk3>()
                    .le(OpenresultJsk3::getOpenResultTime, now)
                    .orderByDesc(OpenresultJsk3::getOpenResultTime);
            Page<OpenresultJsk3> page = openresultJsk3Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjpk10> wrapper = new LambdaQueryWrapper<OpenresultBjpk10>()
                    .le(OpenresultBjpk10::getOpenResultTime, now)
                    .orderByDesc(OpenresultBjpk10::getOpenResultTime);
            Page<OpenresultBjpk10> page = openresultBjpk10Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.CQSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultCqssc> wrapper = new LambdaQueryWrapper<OpenresultCqssc>()
                    .le(OpenresultCqssc::getOpenResultTime, now)
                    .orderByDesc(OpenresultCqssc::getOpenResultTime);
            Page<OpenresultCqssc> page = openresultCqsscService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FC3D.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFc3d> wrapper = new LambdaQueryWrapper<OpenresultFc3d>()
                    .le(OpenresultFc3d::getOpenResultTime, now)
                    .orderByDesc(OpenresultFc3d::getOpenResultTime);
            Page<OpenresultFc3d> page = openresultFc3dService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFt> wrapper = new LambdaQueryWrapper<OpenresultFt>()
                    .le(OpenresultFt::getOpenResultTime, now)
                    .orderByDesc(OpenresultFt::getOpenResultTime);
            Page<OpenresultFt> page = openresultFtService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GD11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGd11x5> wrapper = new LambdaQueryWrapper<OpenresultGd11x5>()
                    .le(OpenresultGd11x5::getOpenResultTime, now)
                    .orderByDesc(OpenresultGd11x5::getOpenResultTime);
            Page<OpenresultGd11x5> page = openresultGd11x5Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.MO6HC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultMo6hc> wrapper = new LambdaQueryWrapper<OpenresultMo6hc>()
                    .le(OpenresultMo6hc::getOpenResultTime, now)
                    .orderByDesc(OpenresultMo6hc::getOpenResultTime);
            Page<OpenresultMo6hc> page = openresultMo6hcService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjkl8> wrapper = new LambdaQueryWrapper<OpenresultBjkl8>()
                    .le(OpenresultBjkl8::getOpenResultTime, now)
                    .orderByDesc(OpenresultBjkl8::getOpenResultTime);
            Page<OpenresultBjkl8> page = openresultBjkl8Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.PCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultPcdd> wrapper = new LambdaQueryWrapper<OpenresultPcdd>()
                    .le(OpenresultPcdd::getOpenResultTime, now)
                    .orderByDesc(OpenresultPcdd::getOpenResultTime);
            Page<OpenresultPcdd> page = openresultPcddService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MFT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mft> wrapper = new LambdaQueryWrapper<OpenresultGs1mft>()
                    .le(OpenresultGs1mft::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mft::getOpenResultTime);
            Page<OpenresultGs1mft> page = openresultGs1mftService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mk3> wrapper = new LambdaQueryWrapper<OpenresultGs1mk3>()
                    .le(OpenresultGs1mk3::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mk3::getOpenResultTime);
            Page<OpenresultGs1mk3> page = openresultGs1mk3Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MLHC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mlhc> wrapper = new LambdaQueryWrapper<OpenresultGs1mlhc>()
                    .le(OpenresultGs1mlhc::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mlhc::getOpenResultTime);
            Page<OpenresultGs1mlhc> page = openresultGs1mlhcService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpk10> wrapper = new LambdaQueryWrapper<OpenresultGs1mpk10>()
                    .le(OpenresultGs1mpk10::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mpk10::getOpenResultTime);
            Page<OpenresultGs1mpk10> page = openresultGs1mpk10Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mssc> wrapper = new LambdaQueryWrapper<OpenresultGs1mssc>()
                    .le(OpenresultGs1mssc::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mssc::getOpenResultTime);
            Page<OpenresultGs1mssc> page = openresultGs1msscService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mkl8> wrapper = new LambdaQueryWrapper<OpenresultGs1mkl8>()
                    .le(OpenresultGs1mkl8::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mkl8::getOpenResultTime);
            Page<OpenresultGs1mkl8> page = openresultGs1mkl8Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1M11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1m11x5> wrapper = new LambdaQueryWrapper<OpenresultGs1m11x5>()
                    .le(OpenresultGs1m11x5::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1m11x5::getOpenResultTime);
            Page<OpenresultGs1m11x5> page = openresultGs1m11x5Service.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpcdd> wrapper = new LambdaQueryWrapper<OpenresultGs1mpcdd>()
                    .le(OpenresultGs1mpcdd::getOpenResultTime, now)
                    .orderByDesc(OpenresultGs1mpcdd::getOpenResultTime);
            Page<OpenresultGs1mpcdd> page = openresultGs1mpcddService.page(new Page<>(1, 1), wrapper);
            if (CollUtil.isNotEmpty(page.getRecords())) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(page.getRecords().get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }
        return null;
    }


    /**
     * 获取指定彩种某一期开奖信息
     * @param lotteryCode
     * @return
     */
    public LotteryCurrQsBO getQsInfo(String lotteryCode, String qs) {
        if (StringUtils.equals(lotteryCode, LotteryCodeEnum.JSK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultJsk3> wrapper = new LambdaQueryWrapper<OpenresultJsk3>()
                    .eq(OpenresultJsk3::getQs, qs);
            List<OpenresultJsk3> list = openresultJsk3Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjpk10> wrapper = new LambdaQueryWrapper<OpenresultBjpk10>()
                    .eq(OpenresultBjpk10::getQs, qs);
            List<OpenresultBjpk10> list = openresultBjpk10Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.CQSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultCqssc> wrapper = new LambdaQueryWrapper<OpenresultCqssc>()
                    .eq(OpenresultCqssc::getQs, qs);
            List<OpenresultCqssc> list = openresultCqsscService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FC3D.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFc3d> wrapper = new LambdaQueryWrapper<OpenresultFc3d>()
                    .eq(OpenresultFc3d::getQs, qs);
            List<OpenresultFc3d> list = openresultFc3dService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.FT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultFt> wrapper = new LambdaQueryWrapper<OpenresultFt>()
                    .eq(OpenresultFt::getQs, qs);
            List<OpenresultFt> list = openresultFtService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GD11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGd11x5> wrapper = new LambdaQueryWrapper<OpenresultGd11x5>()
                    .eq(OpenresultGd11x5::getQs, qs);
            List<OpenresultGd11x5> list = openresultGd11x5Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.MO6HC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultMo6hc> wrapper = new LambdaQueryWrapper<OpenresultMo6hc>()
                    .eq(OpenresultMo6hc::getQs, qs);
            List<OpenresultMo6hc> list = openresultMo6hcService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.BJKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultBjkl8> wrapper = new LambdaQueryWrapper<OpenresultBjkl8>()
                    .eq(OpenresultBjkl8::getQs, qs);
            List<OpenresultBjkl8> list = openresultBjkl8Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.PCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultPcdd> wrapper = new LambdaQueryWrapper<OpenresultPcdd>()
                    .eq(OpenresultPcdd::getQs, qs);
            List<OpenresultPcdd> list = openresultPcddService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MFT.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mft> wrapper = new LambdaQueryWrapper<OpenresultGs1mft>()
                    .eq(OpenresultGs1mft::getQs, qs);
            List<OpenresultGs1mft> list = openresultGs1mftService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MK3.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mk3> wrapper = new LambdaQueryWrapper<OpenresultGs1mk3>()
                    .eq(OpenresultGs1mk3::getQs, qs);
            List<OpenresultGs1mk3> list = openresultGs1mk3Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MLHC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mlhc> wrapper = new LambdaQueryWrapper<OpenresultGs1mlhc>()
                    .eq(OpenresultGs1mlhc::getQs, qs);
            List<OpenresultGs1mlhc> list = openresultGs1mlhcService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPK10.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpk10> wrapper = new LambdaQueryWrapper<OpenresultGs1mpk10>()
                    .eq(OpenresultGs1mpk10::getQs, qs);
            List<OpenresultGs1mpk10> list = openresultGs1mpk10Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MSSC.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mssc> wrapper = new LambdaQueryWrapper<OpenresultGs1mssc>()
                    .eq(OpenresultGs1mssc::getQs, qs);
            List<OpenresultGs1mssc> list = openresultGs1msscService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MKL8.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mkl8> wrapper = new LambdaQueryWrapper<OpenresultGs1mkl8>()
                    .eq(OpenresultGs1mkl8::getQs, qs);
            List<OpenresultGs1mkl8> list = openresultGs1mkl8Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1M11X5.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1m11x5> wrapper = new LambdaQueryWrapper<OpenresultGs1m11x5>()
                    .eq(OpenresultGs1m11x5::getQs, qs);
            List<OpenresultGs1m11x5> list = openresultGs1m11x5Service.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        } else if (StringUtils.equals(lotteryCode, LotteryCodeEnum.GS1MPCDD.getLotteryCode())) {
            LambdaQueryWrapper<OpenresultGs1mpcdd> wrapper = new LambdaQueryWrapper<OpenresultGs1mpcdd>()
                    .eq(OpenresultGs1mpcdd::getQs, qs);
            List<OpenresultGs1mpcdd> list = openresultGs1mpcddService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                LotteryCurrQsBO lotteryCurrQsBO = new LotteryCurrQsBO();
                BeanUtil.copyPropertiesIgnoreNull(list.get(0), lotteryCurrQsBO);
                return lotteryCurrQsBO;
            }
        }
        return null;
    }

    public PageUtils gethistoryByPage(String lotteryCode, Map<String, Object> params) {
        PageUtils pageUtils = null;
        if (StringUtils.equals(LotteryCodeEnum.BJKL8.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultBjkl8Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.BJPK10.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultBjpk10Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.CQSSC.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultCqsscService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.FC3D.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultFc3dService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.FT.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultFtService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GD11X5.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGd11x5Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.JSK3.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultJsk3Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.MO6HC.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultMo6hcService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.PCDD.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultPcddService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MFT.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mftService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MK3.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mk3Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MLHC.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mlhcService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MPK10.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mpk10Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MKL8.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mkl8Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1M11X5.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1m11x5Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GS1MPCDD.getLotteryCode(), lotteryCode)) {
            pageUtils = openresultGs1mpcddService.queryPage(params);

        }
        return pageUtils;
    }
}
