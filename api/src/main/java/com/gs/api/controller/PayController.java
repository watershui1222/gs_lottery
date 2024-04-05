package com.gs.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.commons.entity.CompanyAccount;
import com.gs.commons.service.CompanyAccountService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "充值相关", tags = "充值相关")
@RequestMapping("/deposit")
@RestController
public class PayController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CompanyAccountService companyAccountService;


    @ApiOperation(value = "获取公司入款充值方式")
    @GetMapping("/getCompany")
    public R companyList(HttpServletRequest httpServletRequest) {
        List<CompanyAccount> list = companyAccountService.list(
                new LambdaQueryWrapper<CompanyAccount>()
                        .eq(CompanyAccount::getStatus, 0)
                        .orderByDesc(CompanyAccount::getPxh)
        );

        Map<String, Object> account = new HashMap<>();
        List<JSONObject> arr = new ArrayList<>();
        for (CompanyAccount companyAccount : list) {
            JSONObject object = new JSONObject();
            object.put("accountName", companyAccount.getAccountName());
            object.put("payeeName", companyAccount.getPayeeName());
            object.put("accountNo", companyAccount.getAccountNo());
            object.put("address", companyAccount.getAddress());
            object.put("minAmount", companyAccount.getMinAmount());
            object.put("maxAmount", companyAccount.getMaxAmount());
            object.put("remark", companyAccount.getRemark());
            object.put("type", companyAccount.getType());
            if (account.containsKey(String.valueOf(companyAccount.getType()))) {
                continue;
            }
            account.put(String.valueOf(companyAccount.getType()), object);
            arr.add(object);
        }
        return R.ok().put("list", arr);
    }
}
