package com.gs.task.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.gs.commons.entity.Lottery;
import com.gs.task.enums.LotteryKindEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PaiqiUtil {


    public static List<OpenresultPaiQiData> getPaiQiData(LotteryKindEnum lotteryKindEnum, Lottery lottery) {

        Date startDate = DateUtil.parse(StringUtils.join(DateUtil.formatDate(DateUtil.date()), " ", lotteryKindEnum.getStartTime()));

        List<OpenresultPaiQiData> openresultPaiQiDataList = Lists.newArrayList();
        for (int i = 1; i <= lotteryKindEnum.getCount(); i++) {
            OpenresultPaiQiData openresultPaiQiData = new OpenresultPaiQiData();

            String qs = String.valueOf(i);
            if (String.valueOf(i).length() == 1) {
                qs = StringUtils.join("00", i);
            } else if (String.valueOf(i).length() == 2) {
                qs = StringUtils.join("0", i);
            }

            qs = StringUtils.join(DateUtil.format(startDate, "yyMMdd"), qs);

            DateTime endTime = DateUtil.offsetSecond(startDate, lotteryKindEnum.getRate());
            DateTime closeTime = DateUtil.offsetSecond(endTime, -lottery.getCloseTime());

            openresultPaiQiData.setQs(qs);
            openresultPaiQiData.setPlat_qs(qs);
            openresultPaiQiData.setOpen_status(1);
            openresultPaiQiData.setCurr_count(i);
            openresultPaiQiData.setOpen_time(startDate);
            openresultPaiQiData.setClose_time(closeTime);
            openresultPaiQiData.setCreate_time(DateUtil.date());
            openresultPaiQiData.setUpdate_time(DateUtil.date());
            openresultPaiQiDataList.add(openresultPaiQiData);
            startDate = endTime;
        }
        return openresultPaiQiDataList;
    }
}
