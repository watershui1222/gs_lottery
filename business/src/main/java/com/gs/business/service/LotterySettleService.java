package com.gs.business.service;

import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.excption.BusinessException;

import java.math.BigDecimal;
import java.util.List;

public interface LotterySettleService {

    void settle(LotteryOrder order) throws BusinessException;
}
