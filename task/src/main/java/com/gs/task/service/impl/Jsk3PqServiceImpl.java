package com.gs.task.service.impl;

import com.google.common.collect.Lists;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.task.enums.LotteryKindEnum;
import com.gs.task.service.PqService;
import com.gs.task.util.OpenresultPaiQiData;
import com.gs.task.util.PaiqiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Jsk3PqServiceImpl extends PqService<OpenresultJsk3> {

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    @Override
    public LotteryKindEnum lotteryKindCode() {
        return LotteryKindEnum.JSK3;
    }

    @Override
    public List<OpenresultJsk3> getData() {
        Lottery lottery = this.getLottery();
        List<OpenresultPaiQiData> paiQiData = PaiqiUtil.getPaiQiData(lotteryKindCode(), lottery);
        ArrayList<OpenresultJsk3> resultList = Lists.newArrayList();
        for (OpenresultPaiQiData paiQiDatum : paiQiData) {
            OpenresultJsk3 openresultJsk3 = new OpenresultJsk3();
            openresultJsk3.setQs(paiQiDatum.getQs());
            openresultJsk3.setPlatQs(paiQiDatum.getPlat_qs());
            openresultJsk3.setOpenResult(paiQiDatum.getOpen_result());
            openresultJsk3.setOpenStatus(paiQiDatum.getOpen_status());
            openresultJsk3.setCurrCount(paiQiDatum.getCurr_count());
            openresultJsk3.setOpenTime(paiQiDatum.getOpen_time());
            openresultJsk3.setCloseTime(paiQiDatum.getClose_time());
            openresultJsk3.setOpenResultTime(paiQiDatum.getOpen_result_time());
            openresultJsk3.setCreateTime(paiQiDatum.getCreate_time());
            openresultJsk3.setUpdateTime(paiQiDatum.getUpdate_time());
            resultList.add(openresultJsk3);

        }
        return resultList;
    }

    @Override
    public void saveData() {
        openresultJsk3Service.saveBatch(getData());
    }
}
