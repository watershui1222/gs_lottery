package com.gs.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.api.controller.request.PlatDepositRequest;
import com.gs.api.controller.request.PlatLoginUrlRequest;
import com.gs.api.controller.request.PlatWithdrawRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.PlatClient;
import com.gs.business.pojo.PlatLoginUrlBO;
import com.gs.business.service.EduService;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.Platform;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.EduOrderService;
import com.gs.commons.service.PlatformService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.service.UserPlatService;
import com.gs.commons.utils.IdUtils;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "三方平台相关", tags = "三方平台相关")
@RequestMapping("/plat")
@RestController
public class PlatController {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PlatClient platClient;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private EduService eduService;
    @Autowired
    private EduOrderService eduOrderService;

    @Autowired
    private UserPlatService userPlatService;

    @Autowired
    private PlatformService platformService;


    @ApiOperation(value = "获取额度转换平台列表")
    @GetMapping("/getPlatList")
    public R getPlatList(HttpServletRequest httpServletRequest) {
        List<Platform> platforms = platformService.list(
                new QueryWrapper<Platform>()
                        .select("plat_code,plat_name")
                        .eq("status", 0)
                        .groupBy("plat_code")
                        .orderByDesc("pxh")
        );
        JSONArray array = new JSONArray();
        for (Platform platform : platforms) {
            JSONObject object = new JSONObject();
            object.put("platCode", platform.getPlatCode());
            object.put("platName", platform.getPlatName());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "根据分类获取平台列表")
    @GetMapping("/getPlatByType/{type}")
    public R getPlatByType(HttpServletRequest httpServletRequest, @PathVariable("type") String type) {
        List<Platform> platforms = platformService.list(
                new LambdaQueryWrapper<Platform>()
                        .eq(Platform::getStatus, 0)
                        .eq(Platform::getPlatType, type)
                        .orderByDesc(Platform::getPxh)
        );
        JSONArray array = new JSONArray();
        for (Platform platform : platforms) {
            JSONObject object = new JSONObject();
            object.put("platCode", platform.getPlatCode());
            object.put("platName", platform.getPlatName());
            object.put("subPlatCode", platform.getSubPlatCode());
            object.put("subPlatName", platform.getSubPlatName());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "获取三方平台余额")
    @GetMapping("/getBalancec/{platCode}")
    public R getBalancec(@PathVariable("platCode") String platCode, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, platCode)
        );
        if (userPlat == null) {
            return R.ok().put("balance", 0);
        }
        // 查余额
        BigDecimal amount = platClient.queryBalance(userPlat);
        return R.ok().put("balance", amount);
    }

    @ApiOperation(value = "获取三方平台登录URL")
    @GetMapping("/login")
    public R login(@Validated PlatLoginUrlRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 注册
        UserPlat userPlat = platClient.register(request.getPlatCode(), userName);
        if (userPlat == null) {
            throw new Exception("平台:" + userPlat.getPlatCode() + "注册失败");
        }
        // 获取登录链接
        PlatLoginUrlBO bo = new PlatLoginUrlBO();
        bo.setPlatSubCode(request.getPlatSubCode());
        bo.setGameCode(request.getGameCode());
        bo.setPlatCode(request.getPlatCode());
        bo.setPlatUserName(userPlat.getPlatUserName());
        bo.setUserName(userPlat.getUserName());
        bo.setPlatUserPassword(userPlat.getPlatUserPassword());
        String loginUrl = platClient.getLoginUrl(bo);
        return R.ok().put("loginUrl", loginUrl);
    }

    @ApiOperation(value = "额度转入")
    @PostMapping("/deposit")
    public R deposit(@Validated PlatDepositRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 校验余额是否充足
        UserInfo userInfo = userInfoService.getUserByName(userName);
        BigDecimal amount = new BigDecimal(request.getAmount());
        if (amount.doubleValue() < 1) {
            return R.error("请输入大于1的金额");
        }
        if (amount.doubleValue() > userInfo.getBalance().doubleValue()) {
            return R.error("余额不足");
        }
        // 注册
        UserPlat userPlat = platClient.register(request.getPlatCode(), userName);
        if (userPlat == null) {
            throw new Exception("平台:" + userPlat.getPlatCode() + "注册失败");
        }
        // 生产三方订单号
        String depositOrderNo = platClient.getDepositOrderNo(userPlat.getPlatCode(), amount, userPlat);
        // 生成订单、扣除用户金额
        EduOrder eduOrder = eduService.saveOrderAndSubAmount(userPlat.getUserName(), amount, userPlat.getPlatCode(), depositOrderNo);
        // 调用三方充值
        boolean success = platClient.deposit(userPlat, amount, eduOrder);
        if (success) {
            // 调用三方成功,修改额度订单记录
            eduOrderService.update(
                    new LambdaUpdateWrapper<EduOrder>()
                            .eq(EduOrder::getOrderNo, eduOrder.getOrderNo())
                            .set(EduOrder::getStatus, 0)
                            .set(EduOrder::getUpdateTime, new Date())
            );
            return R.ok();
        } else {
            // 调用失败,联系客服处理
        }
        return R.error("额度转入失败,请联系客服处理");
    }

    @ApiOperation(value = "额度转出")
    @PostMapping("/withdraw")
    public R withdraw(@Validated PlatWithdrawRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 注册
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, request.getPlatCode())
        );
        if (userPlat == null) {
            throw new Exception("额度转出失败");
        }
        // 金额
        BigDecimal amount = new BigDecimal(request.getAmount());
        if (amount.doubleValue() < 1) {
            return R.error("请输入大于1的金额");
        }
        // 查询第三方余额
        BigDecimal platBalance = platClient.queryBalance(userPlat);
        if (amount.doubleValue() > platBalance.doubleValue()) {
            return R.error("第三方余额不足.");
        }
        // 生产三方订单号
        String withdrawOrderNo = platClient.getWithdrawOrderNo(userPlat.getPlatCode(), amount, userPlat);
        // 添加额度转换记录
        Date now = new Date();
        String orderNo = IdUtils.getPlatOutOrderNo();
        EduOrder eduOrder = new EduOrder();
        eduOrder.setUserName(userName);
        eduOrder.setOrderNo(orderNo);
        eduOrder.setPlatOrderNo(withdrawOrderNo);
        eduOrder.setAmount(amount);
        eduOrder.setEduType(1);
        eduOrder.setPlatCode(userPlat.getPlatCode());
        eduOrder.setStatus(-1);
        eduOrder.setCreateTime(now);
        eduOrder.setUpdateTime(now);
        eduOrder.setRemark("[" + userPlat.getPlatCode() + "]额度转出至平台:" + amount + "元");
        eduOrderService.save(eduOrder);
        // 调用三方充值
        boolean success = platClient.withdraw(userPlat, amount, eduOrder);
        if (success) {
            // 调用三方成功,给用户加钱
            eduService.AddMoneyAndTranscationRecord(userName, amount, userPlat.getPlatCode(), withdrawOrderNo, eduOrder.getOrderNo());
            return R.ok();
        } else {
            // 调用失败,联系客服处理
        }
        return R.error("额度转入失败,请联系客服处理");
    }
}
