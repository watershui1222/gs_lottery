package com.gs.task.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.gs.commons.entity.Lottery;
import com.gs.task.enums.LotteryEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class PaiqiUtil {


    public static List<OpenresultPaiQiData> getPaiQiData(LotteryEnum lotteryEnum, Lottery lottery) {

        Date startDate = DateUtil.parse(StringUtils.join(DateUtil.formatDate(DateUtil.date()), " ", lotteryEnum.getStartTime()));

        List<OpenresultPaiQiData> openresultPaiQiDataList = Lists.newArrayList();
        for (int i = 1; i <= lotteryEnum.getCount(); i++) {
            OpenresultPaiQiData openresultPaiQiData = new OpenresultPaiQiData();

            String qs = String.valueOf(i);
            if (String.valueOf(i).length() == 1) {
                qs = StringUtils.join("00", i);
            } else if (String.valueOf(i).length() == 2) {
                qs = StringUtils.join("0", i);
            }

            qs = StringUtils.join(DateUtil.format(startDate, "yyMMdd"), qs);

            DateTime endTime = DateUtil.offsetSecond(startDate, lotteryEnum.getRate());
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
