package com.gs.api.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.PlatClient;
import com.gs.business.pojo.dto.PlatDepositRequest;
import com.gs.business.pojo.dto.PlatLoginUrlRequest;
import com.gs.business.pojo.dto.PlatWithdrawRequest;
import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.EduService;
import com.gs.commons.entity.*;
import com.gs.commons.service.*;
import com.gs.commons.utils.IdUtils;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private EleGameService eleGameService;


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
            object.put("balance", new BigDecimal(0));
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "获取指定平台电子游戏列表")
    @GetMapping("/getElegame/{subPlatCode}")
    public R getElegame(HttpServletRequest httpServletRequest,@PathVariable("subPlatCode") String subPlatCode) {
        String redisKey = "plat:elegame:" + subPlatCode;
//        String redisValue = redisTemplate.opsForValue().get(redisKey);
//        if (StringUtils.isNotBlank(redisValue)) {
//            return R.ok().put("list", JSON.parseArray(redisValue));
//        }
        List<EleGame> list = eleGameService.list(
                new LambdaQueryWrapper<EleGame>()
                        .eq(EleGame::getStatus, 0)
                        .eq(EleGame::getPlatSubCode, subPlatCode)
                        .orderByDesc(EleGame::getPxh)
        );
        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();
        JSONArray array = new JSONArray();
        for (EleGame eleGame : list) {
            JSONObject object = new JSONObject();
            object.put("gameCode", eleGame.getGameCode());
            object.put("gameName", eleGame.getGameName());
            object.put("img", allParamByMap.get("resource_domain") + eleGame.getImg());
            array.add(object);
        }
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(array), 1, TimeUnit.HOURS);
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "根据分类获取平台列表")
    @GetMapping("/getAllPlat")
    public R getPlatByType(HttpServletRequest httpServletRequest) {
        List<Platform> platforms = platformService.list(
                new LambdaQueryWrapper<Platform>()
                        .eq(Platform::getStatus, 0)
                        .orderByDesc(Platform::getPxh)
        );

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        JSONArray array = new JSONArray();
        for (Platform platform : platforms) {
            JSONObject object = new JSONObject();
            object.put("platCode", platform.getPlatCode());
            object.put("platName", platform.getPlatName());
            object.put("subPlatCode", platform.getSubPlatCode());
            object.put("subPlatName", platform.getSubPlatName());
            object.put("type", platform.getPlatType());
            object.put("img", allParamByMap.get("resource_domain") + platform.getImg1());
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
        // 查询平台信息
        Platform platform = platformService.getOne(
                new LambdaQueryWrapper<Platform>()
                        .eq(Platform::getStatus, 0)
                        .eq(Platform::getSubPlatCode, request.getPlatSubCode())
                        .eq(Platform::getPlatCode, request.getPlatCode())
        );
        if (platform == null || platform.getMaintenanceStatus().intValue() == 1) {
            String msg = StringUtils.isNotBlank(platform.getMaintenanceMsg()) ? platform.getMaintenanceMsg() : "平台维护中";
            return R.error(msg);
        }
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
        if (userInfo.getPayStatus().intValue() != 0) {
            return R.error("资金账户被限制,请联系客服");
        }
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


    @ApiOperation(value = "一键转出所有平台")
    @PostMapping("/withdrawAll")
    public R withdrawAll(HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        String redisKey = "user:withdrawall:" + userName;
        if (redisTemplate.hasKey(redisKey)) {
            return R.error("请勿频繁操作");
        }
        redisTemplate.opsForValue().set(redisKey, "true", 1, TimeUnit.MINUTES);
        // 查询用户已注册的所有平台
        List<UserPlat> userPlats = userPlatService.list(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
        );
        for (UserPlat userPlat : userPlats) {
            try {
                // 查询第三方余额
                BigDecimal amount = platClient.queryBalance(userPlat);
                if (amount.doubleValue() < 1) {
                    continue;
                }
                amount = BigDecimal.valueOf(amount.intValue());
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
                } else {
                    // 调用失败,联系客服处理
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }

    @ApiOperation(value = "插入游戏")
    @GetMapping("/insertGame")
    public R insertGame(HttpServletRequest httpServletRequest) throws Exception {
        List<EleGame> list = new ArrayList<>();
        String path = "C:\\Users\\69000\\Desktop\\elegame\\bbin";
        File[] ls = FileUtil.ls(path);
//        for (File l : ls) {
//
//            EleGame eleGame = new EleGame();
//            System.out.println(l.getName());
//            String[] s = l.getName().split("_");
//            eleGame.setPlatCode("BBIN");
//            eleGame.setPlatSubCode("");
//            eleGame.setGameCode(s[0]);
//            eleGame.setGameName(s[1]);
//            eleGame.setImg("/gs/platform/elegame/bbin/" + l.getName());
//            eleGame.setStatus(0);
//            eleGame.setCreateTime(new Date());
//            eleGame.setUpdateTime(new Date());
//            eleGame.setPxh(0);
//
//            list.add(eleGame);
//        }

        list.clear();

        ls = FileUtil.ls("C:\\Users\\69000\\Desktop\\elegame\\ly");

//        for (File l : ls) {
//            EleGame eleGame = new EleGame();
//            System.out.println(l.getName());
//            String[] s = l.getName().split("_");
//            eleGame.setPlatCode("LY");
//            eleGame.setPlatSubCode("");
//            eleGame.setGameCode(s[0]);
//            eleGame.setGameName(s[1].replace(".png", ""));
//            eleGame.setImg("/gs/platform/elegame/ly/" + l.getName());
//            eleGame.setStatus(0);
//            eleGame.setCreateTime(new Date());
//            eleGame.setUpdateTime(new Date());
//            eleGame.setPxh(0);
//
//            list.add(eleGame);
//        }

        list.clear();

        ls = FileUtil.ls("C:\\Users\\69000\\Desktop\\elegame\\ag");

//        for (File l : ls) {
//            EleGame eleGame = new EleGame();
//            System.out.println(l.getName());
//            String[] s = l.getName().split("_");
//            eleGame.setPlatCode("AG");
//            eleGame.setPlatSubCode("AGELE");
//            eleGame.setGameCode(s[0]);
//            eleGame.setGameName(s[1].replace(".png", ""));
//            eleGame.setImg("/gs/platform/elegame/ag/" + l.getName());
//            eleGame.setStatus(0);
//            eleGame.setCreateTime(new Date());
//            eleGame.setUpdateTime(new Date());
//            eleGame.setPxh(0);
//
//            list.add(eleGame);
//        }

        list.clear();

        ls = FileUtil.ls("C:\\Users\\69000\\Desktop\\elegame\\ky");

        for (File l : ls) {
            EleGame eleGame = new EleGame();
            System.out.println(l.getName());
            String[] s = l.getName().split("_");
            eleGame.setPlatCode("KY");
            eleGame.setPlatSubCode("KY");
            eleGame.setGameCode(s[0]);
            eleGame.setGameName(s[1].replace(".png", ""));
            eleGame.setImg("/gs/platform/elegame/ky/" + l.getName());
            eleGame.setStatus(0);
            eleGame.setCreateTime(new Date());
            eleGame.setUpdateTime(new Date());
            eleGame.setPxh(0);

            list.add(eleGame);
        }
        eleGameService.saveBatch(list);
        return R.ok();
    }
}
