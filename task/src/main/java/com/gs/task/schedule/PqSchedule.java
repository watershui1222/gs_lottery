package com.gs.task.schedule;

import cn.hutool.core.date.DateUtil;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.task.client.LotteryDataClient;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class PqSchedule {

    @Autowired
    private LotteryDataClient lotteryDataClient;


    @Scheduled(cron = "${paiqiTask.cron.jsk3}")
    public void jsk3Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.JSK3.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.cqssc}")
    public void cqsscPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.CQSSC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "${paiqiTask.cron.ft}")
    public void ftPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.FT.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gd11x5}")
    public void gd11x5Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GD11X5.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "${paiqiTask.cron.bjkl8}")
    public void bjkl8Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJKL8.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.pcdd}")
    public void pcddPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.PCDD.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "${paiqiTask.cron.bjpk10}")
    public void bjpk10Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJPK10.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.mo6hc}")
    public void mo6hcPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.MO6HC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }




    @Scheduled(cron = "${paiqiTask.cron.gs1mk3}")
    public void gs1mk3Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MK3.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }



    @Scheduled(cron = "${paiqiTask.cron.gs1mft}")
    public void gs1mftPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MFT.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mssc}")
    public void gs1msscPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MSSC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mpk10}")
    public void gs1mPK10Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MPK10.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "${paiqiTask.cron.gs1mlhc}")
    public void gs1mlhcPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MLHC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mkl8}")
    public void gs1mKl8Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MKL8.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1m11x5}")
    public void gs1m11x5Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1M11X5.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mpcdd}")
    public void gs1mpcddPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MPCDD.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mfc3d}")
    public void gs1mfc3dPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MFC3D.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "${paiqiTask.cron.gs1mpl3}")
    public void gs1mpl3Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GS1MPL3.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }



}
