package com.gs.business.service;

import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.entity.UserInfo;

import java.math.BigDecimal;
import java.util.List;

public interface LotteryBetService {

    void bet(UserInfo info, BigDecimal amount, List<LotteryOrder> orders) throws Exception;

    void cancel(LotteryOrder lotteryOrder) throws Exception;
}
