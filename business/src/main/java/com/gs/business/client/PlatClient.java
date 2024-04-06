package com.gs.business.client;

import com.gs.business.service.PlatService;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import com.gs.commons.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlatClient {

    @Autowired
    private PlatService kyService;

    public BigDecimal queryBalance(String platCode, String userName) throws Exception {
        BigDecimal balance = new BigDecimal(0);
        if (StringUtils.equals(platCode, "ky")) {
            UserPlat userPlat = kyService.registerBalance(userName);
            balance =  kyService.queryBalance(userPlat);
        }
        return balance;
    }


    public String getLoginUrl(String platCode, String userName) throws Exception {
        if (StringUtils.equals(platCode, "ky")) {
            UserPlat userPlat = kyService.registerBalance(userName);
            return kyService.getLoginUrl(userPlat);
        }
        return null;
    }
}
