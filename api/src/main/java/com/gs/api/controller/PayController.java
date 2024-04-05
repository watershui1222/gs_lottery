package com.gs.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.api.controller.request.CompanyDepositRequest;
import com.gs.api.controller.request.CompanyVirDepositRequest;
import com.gs.api.controller.request.DepositRecordRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.commons.constants.Constant;
import com.gs.commons.entity.CompanyAccount;
import com.gs.commons.entity.CompanyVirtual;
import com.gs.commons.entity.Deposit;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.CompanyAccountService;
import com.gs.commons.service.CompanyVirtualService;
import com.gs.commons.service.DepositService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.IdUtils;
import com.gs.commons.utils.MsgUtil;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    private CompanyVirtualService companyVirtualService;

    @Autowired
    private DepositService depositService;


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
            object.put("channelId", companyAccount.getId());
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


    @ApiOperation(value = "获取虚拟货币充值方式")
    @GetMapping("/getvirtual")
    public R virtualList(HttpServletRequest httpServletRequest) {
        List<CompanyVirtual> list = companyVirtualService.list(
                new LambdaQueryWrapper<CompanyVirtual>()
                        .eq(CompanyVirtual::getStatus, 0)
                        .orderByDesc(CompanyVirtual::getPxh)
        );

        Map<String, Object> account = new HashMap<>();
        List<JSONObject> arr = new ArrayList<>();
        for (CompanyVirtual companyAccount : list) {
            JSONObject object = new JSONObject();
            object.put("channelId", companyAccount.getId());
            object.put("name", companyAccount.getChannelName());
            object.put("type", companyAccount.getChannelType());
            object.put("exchangeRate", companyAccount.getExchangeRate());
            object.put("address", companyAccount.getAccountNo());
            object.put("remark", companyAccount.getRemark());
            object.put("minAmount", companyAccount.getMinAmount());
            object.put("maxAmount", companyAccount.getMaxAmount());
            if (account.containsKey(String.valueOf(companyAccount.getChannelType()))) {
                continue;
            }
            account.put(String.valueOf(companyAccount.getChannelType()), object);
            arr.add(object);
        }
        return R.ok().put("list", arr);
    }


    @ApiOperation(value = "公司入款充值")
    @PostMapping("/companyDeposit")
    public R companyDeposit(@Validated CompanyDepositRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo user = userInfoService.getUserByName(userName);

        CompanyAccount companyAccount = companyAccountService.getById(request.getChannelId());
        BigDecimal amount = new BigDecimal(request.getAmount());

        // 校验是否还有未充值订单
        List<Deposit> deposits = depositService.list(
                new LambdaQueryWrapper<Deposit>()
                        .eq(Deposit::getUserName, user.getUserName())
                        .eq(Deposit::getStatus, 0)
        );
        if (CollUtil.isNotEmpty(deposits)) {
            return R.error(MsgUtil.get("system.deposit.company.err1"));
        }

        Date now = new Date();
        Deposit deposit = new Deposit();
        deposit.setUserName(user.getUserName());
        deposit.setOrderNo(IdUtils.getDepositOrderNo());
        deposit.setAmount(amount);
        deposit.setDepositType(companyAccount.getType());
        deposit.setCreateTime(now);
        deposit.setCheckTime(null);
        deposit.setUpdateTime(null);
        deposit.setOperName(null);
        deposit.setRemark(null);
        String accountDetail = companyAccount.getAccountName()
                + "|"
                + companyAccount.getPayeeName()
                + "|"
                + companyAccount.getAccountNo()
                + "|"
                + companyAccount.getAddress();
        deposit.setAccountDetail(accountDetail);
        String depositDetail = "存款人姓名:[" + request.getName() + "],转账备注:" + request.getRemark();
        deposit.setDepositDetail(depositDetail);
        deposit.setStatus(0);
        depositService.save(deposit);
        return R.ok(MsgUtil.get("system.deposit.company.success"));
    }


    @ApiOperation(value = "虚拟货币充值")
    @PostMapping("/companyVirDeposit")
    public R companyVirDeposit(@Validated CompanyVirDepositRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo user = userInfoService.getUserByName(userName);

        CompanyVirtual companyVirtual = companyVirtualService.getById(request.getChannelId());
        // 计算汇率
        BigDecimal amount = new BigDecimal(request.getAmount());
        amount = NumberUtil.mul(amount, companyVirtual.getExchangeRate());
        // 校验是否还有未充值订单
        List<Deposit> deposits = depositService.list(
                new LambdaQueryWrapper<Deposit>()
                        .eq(Deposit::getUserName, user.getUserName())
                        .eq(Deposit::getStatus, 0)
        );
        if (CollUtil.isNotEmpty(deposits)) {
            return R.error(MsgUtil.get("system.deposit.company.err1"));
        }

        Date now = new Date();
        Deposit deposit = new Deposit();
        deposit.setUserName(user.getUserName());
        deposit.setOrderNo(IdUtils.getDepositOrderNo());
        deposit.setAmount(amount);
        deposit.setDepositType(4);
        deposit.setCreateTime(now);
        deposit.setCheckTime(null);
        deposit.setUpdateTime(null);
        deposit.setOperName(null);
        deposit.setRemark(null);
        String channelType = companyVirtual.getChannelType().intValue() == 1 ? "TRC20" : "ERC20";
        String accountDetail = companyVirtual.getChannelName()
                + "|"
                + channelType
                + "|"
                + companyVirtual.getAccountNo();
        deposit.setAccountDetail(accountDetail);
        String depositDetail = "交易ID:[" + request.getTrxId() + "]";
        deposit.setDepositDetail(depositDetail);
        deposit.setStatus(0);
        depositService.save(deposit);
        return R.ok(MsgUtil.get("system.deposit.company.success"));
    }


    @ApiOperation(value = "用户充值记录")
    @GetMapping("/record")
    public R recordList(DepositRecordRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        PageUtils page = depositService.queryPage(params);
        List<Deposit> list = (List<Deposit>) page.getList();

        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> depositTypeMap = new HashMap<>();
            depositTypeMap.put(1, "银行卡转账");
            depositTypeMap.put(2, "微信");
            depositTypeMap.put(3, "支付宝");
            depositTypeMap.put(4, "虚拟货币");
            depositTypeMap.put(5, "在线支付");
            JSONArray arr = new JSONArray();
            for (Deposit temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("time", temp.getCreateTime());
                obj.put("amount", temp.getAmount());
                obj.put("checkTime", temp.getCheckTime());
                obj.put("status", temp.getStatus());
                obj.put("remark", temp.getRemark());
                obj.put("orderId", temp.getOrderNo());
                obj.put("depositTypeStr", depositTypeMap.getOrDefault(temp.getDepositType(), "未知"));
                obj.put("depositType",temp.getDepositType());
                arr.add(obj);
            }
            page.setList(arr);
        }

        return R.ok().put("page", page);
    }
}
