package com.gs.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.api.controller.VO.LotteryHandicapVo;
import com.gs.api.controller.VO.LotteryOddsVo;
import com.gs.api.controller.VO.LotteryPlayVo;
import com.gs.api.controller.request.LotteryBetRequest;
import com.gs.api.controller.request.LotteryOrderListRequest;
import com.gs.api.controller.request.LotteryTimeRequest;
import com.gs.api.controller.request.OpenResultHistoryRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.LotteryClient;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.business.pojo.RecommendVo;
import com.gs.business.service.LotteryBetService;
import com.gs.business.utils.lottery.LHCBetVerifyUtil;
import com.gs.business.utils.lottery.SYX5BetVerifyUtil;
import com.gs.commons.constants.Constant;
import com.gs.commons.entity.*;
import com.gs.commons.service.*;
import com.gs.commons.utils.IdUtils;
import com.gs.commons.utils.PageUtils;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "彩票相关", tags = "彩票相关")
@RequestMapping("/lottery")
@RestController
public class LotteryController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryHandicapService lotteryHandicapService;

    @Autowired
    private LotteryPlayService lotteryPlayService;

    @Autowired
    private LotteryOddsService lotteryOddsService;
    @Autowired
    private OpenresultBjkl8Service openresultBjkl8Service;
    @Autowired
    private OpenresultBjpk10Service openresultBjpk10Service;
    @Autowired
    private OpenresultCqsscService openresultCqsscService;
    @Autowired
    private OpenresultFc3dService openresultFc3dService;
    @Autowired
    private OpenresultFtService openresultFtService;
    @Autowired
    private OpenresultGd11x5Service openresultGd11x5Service;
    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;
    @Autowired
    private OpenresultMo6hcService openresultMo6hcService;
    @Autowired
    private OpenresultPcddService openresultPcddService;
    @Autowired
    private OpenresultGs1mftService openresultGs1mftService;
    @Autowired
    private OpenresultGs1mk3Service openresultGs1mk3Service;
    @Autowired
    private OpenresultGs1mlhcService openresultGs1mlhcService;
    @Autowired
    private OpenresultGs1mpk10Service openresultGs1mpk10Service;
    @Autowired
    private OpenresultGs1msscService openresultGs1msscService;
    @Autowired
    private OpenresultGs1mkl8Service openresultGs1mkl8Service;
    @Autowired
    private OpenresultGs1m11x5Service openresultGs1m11x5Service;
    @Autowired
    private OpenresultGs1mpcddService openresultGs1mpcddService;
    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private SysParamService sysParamService;
    @Autowired
    private LotteryBetService lotteryBetService;
    @Autowired
    private LotteryClient lotteryClient;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;


    @ApiOperation(value = "获取所有彩种")
    @GetMapping("/getAllLottery")
    public R getAllLottery() {
        List<Lottery> list = lotteryService.list(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getStatus, 0)
                        .orderByDesc(Lottery::getPxh)
        );
        JSONArray array = new JSONArray();
        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();
        for (Lottery lottery : list) {
            JSONObject object = new JSONObject();
            object.put("lotteryCode", lottery.getLotteryCode());
            object.put("lotteryName", lottery.getLotteryName());
            object.put("remark", lottery.getRemark());
            object.put("img", allParamByMap.get("resource_domain") + lottery.getImg());
            object.put("lotteryType", lottery.getLotteryType());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "获取指定彩种下的所有盘口以及玩法")
    @GetMapping("/getAllPlay/{lotteryCode}")
    public R getAllPlay(@PathVariable("lotteryCode") String lotteryCode) {
        // 获取彩种信息
        Lottery lottery = lotteryService.getOne(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getLotteryCode, lotteryCode)
        );

        // 从缓存读取 缓存没有去数据库读取
        String redisKey = "lottery:plays:" + lotteryCode;
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(redisValue)) {
            List<LotteryHandicapVo> lotteryHandicapVoList = JSONArray.parseArray(redisValue, LotteryHandicapVo.class);
            return R.ok().put("plays", lotteryHandicapVoList).put("lotteryType", lottery.getLotteryType()).put("lotteryName", lottery.getLotteryName());
        }

        // 获取彩种下的所有盘口
        List<LotteryHandicap> handicaps = lotteryHandicapService.list(
                new LambdaQueryWrapper<LotteryHandicap>()
                        .eq(LotteryHandicap::getLotteryCode, lotteryCode)
                        .eq(LotteryHandicap::getStatus, 0)
                        .orderByDesc(LotteryHandicap::getPxh)
        );
        // 获取彩种下的所有玩法
        List<LotteryPlay> plays = lotteryPlayService.list(
                new LambdaQueryWrapper<LotteryPlay>()
                        .eq(LotteryPlay::getLotteryCode, lotteryCode)
                        .eq(LotteryPlay::getStatus, 0)
                        .orderByDesc(LotteryPlay::getPxh)
        );
        Map<String, List<LotteryPlayVo>> lotteryPlayMap = new HashMap<>();
        // 根据盘口代码进行分组
        for (LotteryPlay play : plays) {
            String handicapCode = play.getHandicapCode();
            List<LotteryPlayVo> lotteryPlayVos = lotteryPlayMap.getOrDefault(handicapCode, new ArrayList<>());

            LotteryPlayVo lotteryPlayVo = new LotteryPlayVo();
            lotteryPlayVo.setPlayCode(play.getPlayCode());
            lotteryPlayVo.setPlayName(play.getPlayName());
            lotteryPlayVo.setWfts(play.getWfts());
            lotteryPlayVo.setZjsm(play.getZjsm());
            lotteryPlayVo.setFl(play.getFl());
            lotteryPlayVos.add(lotteryPlayVo);

            lotteryPlayMap.put(handicapCode, lotteryPlayVos);
        }

        List<LotteryHandicapVo> lotteryHandicapVoList = new ArrayList<>();
        for (LotteryHandicap handicap : handicaps) {
            // 盘口下没有玩法 跳过
            List<LotteryPlayVo> lotteryPlayVos = lotteryPlayMap.get(handicap.getHandicapCode());
            if (CollUtil.isEmpty(lotteryPlayVos)) {
                continue;
            }
            LotteryHandicapVo lotteryHandicapVo = new LotteryHandicapVo();
            lotteryHandicapVo.setHandicapCode(handicap.getHandicapCode());
            lotteryHandicapVo.setHandicapName(handicap.getHandicapName());
            lotteryHandicapVo.setPlays(lotteryPlayVos);
            lotteryHandicapVoList.add(lotteryHandicapVo);
        }

        // 写入缓存
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(lotteryHandicapVoList));
        return R.ok().put("plays", lotteryHandicapVoList).put("lotteryCode", lottery.getLotteryCode()).put("lotteryName", lottery.getLotteryName());
    }


    @ApiOperation(value = "获取指定彩种下所有玩法的号码以及赔率")
    @GetMapping("/getAllOdds/{lotteryCode}/{playCode}")
    public R getAllOdds(@PathVariable("lotteryCode") String lotteryCode, @PathVariable("playCode") String playCode) {
        // 获取彩种信息
//        Lottery lottery = lotteryService.getOne(
//                new LambdaQueryWrapper<Lottery>()
//                        .eq(Lottery::getLotteryCode, lotteryCode)
//        );

        // 从缓存读取 缓存没有去数据库读取
        String redisKey = "lottery:odds:" + lotteryCode + ":" + playCode;
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(redisValue)) {
            List<LotteryOddsVo> lotteryHandicapVoList = JSONArray.parseArray(redisValue, LotteryOddsVo.class);
            return R.ok().put("odds", lotteryHandicapVoList);
        }

        List<LotteryOdds> oddsList = lotteryOddsService.list(
                new LambdaQueryWrapper<LotteryOdds>()
                        .eq(LotteryOdds::getLotteryCode, lotteryCode)
                        .eq(LotteryOdds::getPlayCode, playCode)
                        .eq(LotteryOdds::getStatus, 0)
                        .orderByDesc(LotteryOdds::getPxh)
        );

        List<LotteryOddsVo> list = new ArrayList<>();
        for (LotteryOdds lotteryOdds : oddsList) {
            LotteryOddsVo lotteryOddsVo = new LotteryOddsVo();
            lotteryOddsVo.setName(lotteryOdds.getHmName());
            lotteryOddsVo.setOdds(lotteryOdds.getOdds());
            lotteryOddsVo.setCode(lotteryOdds.getHmCode());
            lotteryOddsVo.setG(lotteryOdds.getGroupName());
            lotteryOddsVo.setId(lotteryOdds.getId());
            list.add(lotteryOddsVo);
        }
        // 写入缓存
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(list));
        return R.ok().put("odds", list);
    }


    @ApiOperation(value = "历史开奖")
    @GetMapping("/openHistory/list")
    public R openHistoryList(OpenResultHistoryRequest request, HttpServletRequest httpServletRequest) {
        Lottery lottery = lotteryService.getLotteryInfo(request.getLotteryCode());
        if (lottery == null) {
            return R.error();
        }

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        request.setDateStr(StringUtils.isEmpty(request.getDateStr()) ? "4" : request.getDateStr());

        //1:今天 2:昨天 3:一周内 4:一月内
        Date startDate = new Date();
        Date endTime = new Date();
        if (StringUtils.isNotBlank(request.getDateStr())) {
            if (StringUtils.equals(request.getDateStr(), "2")) {
                startDate = DateUtil.offsetDay(startDate, -1);
                endTime = DateUtil.endOfDay(startDate);
            } else if (StringUtils.equals(request.getDateStr(), "3")) {
                startDate = DateUtil.offsetWeek(startDate, -1);
            } else if (StringUtils.equals(request.getDateStr(), "4")) {
                startDate = DateUtil.offsetMonth(startDate, -1);
            }
        }
        Date begin = DateUtil.beginOfDay(startDate);
        params.put("startTime", begin);
        params.put("nowTime", endTime);


        PageUtils pageUtils = lotteryClient.gethistoryByPage(request.getLotteryCode(), params);

        return R.ok().put("page", pageUtils).put("lotteryType", lottery.getLotteryType());

    }


    @ApiOperation(value = "彩票投注记录")
    @GetMapping("/lotteryOrder/list")
    public R lotteryOrderList(LotteryOrderListRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        params.put("lotteryCode", request.getLotteryCode());
        params.put("orderStatus", request.getOrderStatus());

        //1:今天 2:昨天 3:一周内 4:一月内
        Date startDate = new Date();
        Date endTime = new Date();
        if (StringUtils.isNotBlank(request.getDateStr())) {
            if (StringUtils.equals(request.getDateStr(), "2")) {
                startDate = DateUtil.offsetDay(startDate, -1);
                endTime = startDate;
            } else if (StringUtils.equals(request.getDateStr(), "3")) {
                startDate = DateUtil.offsetWeek(startDate, -1);
            } else if (StringUtils.equals(request.getDateStr(), "4")) {
                startDate = DateUtil.offsetMonth(startDate, -1);
            }
        }
        Date begin = DateUtil.beginOfDay(startDate);
        Date end = DateUtil.endOfDay(endTime);

        params.put("startTime", begin);
        params.put("endTime", end);


        Map<String, String> paramsMap = sysParamService.getAllParamByMap();
        String resourceDomain = MapUtil.getStr(paramsMap, "resource_domain");

        List<Lottery> list = lotteryService.list(Wrappers.lambdaQuery(Lottery.class));
        Map<String, Lottery> lotteryMap = list.stream().collect(Collectors.toMap(Lottery::getLotteryCode, Function.identity()));


        PageUtils page = lotteryOrderService.queryPage(params);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<LotteryOrder> pageList = (List<LotteryOrder>) page.getList();
            JSONArray jsonArray = new JSONArray();
            for (LotteryOrder lotteryOrder : pageList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderNo", lotteryOrder.getOrderNo());
                jsonObject.put("lotteryName", lotteryOrder.getLotteryName());
                jsonObject.put("gameCode", lotteryOrder.getLotteryCode());
                Lottery lottery = lotteryMap.get(lotteryOrder.getLotteryCode());
                jsonObject.put("lotteryImg", null == lottery || StringUtils.isEmpty(lottery.getImg()) ? "" : resourceDomain + StringUtils.defaultString(lottery.getImg()));
                jsonObject.put("playName", lotteryOrder.getPlayName());
                jsonObject.put("qs", lotteryOrder.getQs());
                jsonObject.put("betAmount", lotteryOrder.getBetAmount());
                jsonObject.put("betTime", lotteryOrder.getBetTime());
                jsonObject.put("bonusAmount", lotteryOrder.getBonusAmount());
                jsonObject.put("profitAmount", lotteryOrder.getProfitAmount());
                jsonObject.put("openResult", lotteryOrder.getOpenResult());
                jsonObject.put("betContent", lotteryOrder.getBetContent());
                jsonObject.put("orderStatus", lotteryOrder.getOrderStatus());
                jsonObject.put("odds", lotteryOrder.getOdds());
                jsonObject.put("lotteryType", lottery.getLotteryType());
                jsonArray.add(jsonObject);

            }
            page.setList(jsonArray);
        }


        List<LotteryOrder> queryList = lotteryOrderService.queryList(params);

        double betAmountSum = 0;
        double profitAmountSum = 0;
        double bonusAmountSum = 0;
        if (CollUtil.isNotEmpty(queryList)) {
            betAmountSum = queryList.stream().mapToDouble(value -> value.getBetAmount().doubleValue()).sum();
            profitAmountSum = queryList.stream().mapToDouble(value -> value.getProfitAmount().doubleValue()).sum();
            bonusAmountSum = queryList.stream().mapToDouble(value -> value.getBonusAmount().doubleValue()).sum();
        }

        return R.ok().put("page", page)
                .put("betAmountSum", betAmountSum)
                .put("profitAmountSum", profitAmountSum)
                .put("bonusAmountSum", bonusAmountSum);
    }


    @ApiOperation(value = "获取当前期和上期开奖")
    @GetMapping("/lotteryQsTime")
    public R lotteryTime(@Validated LotteryTimeRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        LotteryCurrQsBO currentQsData = lotteryClient.getCurrQs(request.getLotteryCode());
        // 当前期
        JSONObject nowQsJson = new JSONObject();
        String nowQs = (null == currentQsData) ? "" : currentQsData.getQs();
        long nowCloseSeconds = (null == currentQsData) ? -1L : currentQsData.getCloseTime().getTime();
        long nowOpenSeconds = (null == currentQsData) ? -1L : currentQsData.getOpenResultTime().getTime();
        nowQsJson.put("qs", nowQs);
        nowQsJson.put("closeTs", nowCloseSeconds);
        nowQsJson.put("openTs", nowOpenSeconds);
        nowQsJson.put("nowTs", System.currentTimeMillis());

        return R.ok().put("nowQs", nowQsJson);
    }

    @ApiOperation(value = "彩票投注")
    @PostMapping("/bet")
    public R bet(@Validated LotteryBetRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        String redisKey = "lottery:bet:" + userName;
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(redisKey, "true", 2, TimeUnit.SECONDS);
        if (!flag) {
            return R.error("请勿频繁操作");
        }

        // 总投注额
        BigDecimal betAmount = new BigDecimal("0");
        // 投注期号
        String qs = request.getQs();
        // 彩种代码
        String lotterCode = request.getLotterCode();
        // 获取彩种信息
        Lottery lottery = lotteryService.getLotteryInfo(lotterCode);
        if (lottery == null || lottery.getStatus().intValue() == 1) {
            return R.error("获取彩种信息错误!");
        }
        // 获取当前期数信息
        LotteryCurrQsBO currQs = lotteryClient.getCurrQs(lottery.getLotteryCode());
        if (currQs == null) {
            return R.error("已封盘");
        }
        Date now = new Date();
        if (now.getTime() > currQs.getCloseTime().getTime()) {
            return R.error("已封盘");
        }
        if (!StringUtils.equals(currQs.getQs(), qs)) {
            return R.error("当前最新期数为:[" + currQs.getQs() + "],请重新投注");
        }

        // 投注内容
        List<LotteryOrder> orders = new ArrayList<>();
        String betContent = HtmlUtil.unescape(request.getBetContent());
        JSONArray betContentArr = JSON.parseArray(betContent);
        // 获取所有投注项信息
        Set<String> oddsIds = new HashSet<>();
        for (int i = 0; i < betContentArr.size(); i++) {
            JSONObject betContentObj = betContentArr.getJSONObject(i);
            String oddsId = betContentObj.getString("oddsId");
            oddsIds.add(oddsId);
        }
        List<LotteryOdds> lotteryOddsList = lotteryOddsService.list(
                new LambdaQueryWrapper<LotteryOdds>()
                        .in(LotteryOdds::getId, oddsIds)
                        .eq(LotteryOdds::getLotteryCode, lotterCode)
        );
        if (CollUtil.isEmpty(lotteryOddsList)) {
            throw new Exception("获取投注项内容失败");
        }
        Map<String, LotteryOdds> lotteryOddsMap = lotteryOddsList.stream().collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item));

        for (int i = 0; i < betContentArr.size(); i++) {
            JSONObject betContentObj = betContentArr.getJSONObject(i);
            String playCode = betContentObj.getString("playCode");
            String hm = betContentObj.getString("hm");
            String oddsId = betContentObj.getString("oddsId");
            BigDecimal amount = betContentObj.getBigDecimal("amount");
            betAmount = NumberUtil.add(betAmount, amount);

            LotteryOdds lotteryOdds = lotteryOddsMap.get(oddsId);
            if (lotteryOdds == null || !StringUtils.equals(lotteryOdds.getPlayCode(), playCode)) {
                throw new Exception("非法参数");
            }

            // 校验组选、直选规则
            checkPlayRules(betContentObj, lotteryOdds, lottery);

            // 组装投注记录
            LotteryOrder order = new LotteryOrder();
            order.setUserName(userName);
            order.setOrderNo(IdUtils.getLotteryOrderNo());
            order.setLotteryCode(lottery.getLotteryCode());
            order.setLotteryName(lottery.getLotteryName());
            order.setHandicapCode(lotteryOdds.getHandicapCode());
            order.setPlayCode(playCode);
            order.setPlayName(lotteryOdds.getPlayName());
            order.setBetContent(hm);
            order.setOdds(lotteryOdds.getOdds());
            order.setQs(currQs.getQs());
            order.setBetAmount(amount);
            order.setBonusAmount(new BigDecimal("0"));
            order.setProfitAmount(new BigDecimal("0"));
            order.setBetTime(now);
            order.setSettleTime(null);
            order.setSettleStatus(0);
            order.setOrderStatus(0);
            order.setOpenResult(null);
            order.setOpenResultTime(currQs.getOpenResultTime());
            order.setSettleGroup(RandomUtil.randomInt(0, 9));
            order.setLotteryType(lottery.getLotteryType());
            orders.add(order);
        }

        // 用户信息
        UserInfo user = userInfoService.getUserByName(userName);
        if (betAmount.doubleValue() > user.getBalance().doubleValue()) {
            return R.error("账户余额不足");
        }

        lotteryBetService.bet(user, betAmount, orders);
        redisTemplate.delete(redisKey);
        return R.ok("投注成功!");
    }

    /**
     * 校验投注项规则
     *
     * @param betContentObj
     */
    private void checkPlayRules(JSONObject betContentObj, LotteryOdds lotteryOdds, Lottery lottery) {
        if (lottery.getLotteryType().intValue() == 4) {
            LHCBetVerifyUtil.verify(betContentObj, lotteryOdds);
        } else if (lottery.getLotteryType().intValue() == 6) {
            SYX5BetVerifyUtil.verify(betContentObj, lotteryOdds);
        }
    }


    @ApiOperation(value = "取消订单")
    @GetMapping("/cancel/{orderNo}")
    public R cancelOrder(HttpServletRequest httpServletRequest, @PathVariable("orderNo") String orderNo) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 查询订单信息
        LotteryOrder lotteryOrder = lotteryOrderService.getOne(
                new LambdaQueryWrapper<LotteryOrder>()
                        .eq(LotteryOrder::getOrderNo, orderNo)
                        .eq(LotteryOrder::getUserName, userName)
        );
        if (lotteryOrder == null || lotteryOrder.getOrderStatus().intValue() != 0) {
            throw new Exception("未获取到订单[" + orderNo + "]信息或该订单已完成!");
        }
        // 查询当前期
        LotteryCurrQsBO currQs = lotteryClient.getCurrQs(lotteryOrder.getLotteryCode());
        if (!StringUtils.equals(lotteryOrder.getQs(), currQs.getQs())) {
            return R.error("已封盘,无法撤销订单");
        }
        // 修改订单
        lotteryBetService.cancel(lotteryOrder);
        return R.ok();
    }

    @ApiOperation(value = "获取推荐彩种")
    @GetMapping("/getRecommendLottery")
    public R getRecommendLottery(HttpServletRequest httpServletRequest) {
        List<Lottery> list = lotteryService.list(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getStatus, 0)
                        .eq(Lottery::getRecommend, 0)
                        .orderByDesc(Lottery::getPxh)
        );
        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        List<CompletableFuture> futures = new ArrayList<>();

        List<RecommendVo> recommendVoList = new ArrayList<>();
        for (Lottery lottery : list) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                RecommendVo recommendVo = new RecommendVo();
                // 获取当前期
                LotteryCurrQsBO currQs = lotteryClient.getCurrQs(lottery.getLotteryCode());
                // 获取上一期
                LotteryCurrQsBO lastQs = lotteryClient.getLastQs(lottery.getLotteryCode());

                long closeTime = -1;
                String qs = "";
                long openResultTime = -1;
                if (null != currQs) {
                    closeTime = currQs.getCloseTime().getTime();
                    qs = currQs.getQs();
                    openResultTime = currQs.getOpenResultTime().getTime();
                }
                recommendVo.setCloseTime(closeTime);
                recommendVo.setServerTime(System.currentTimeMillis());
                recommendVo.setOpenResultTime(openResultTime);

                recommendVo.setQs(qs);
                recommendVo.setLotteryName(lottery.getLotteryName());
                recommendVo.setLotteryCode(lottery.getLotteryCode());
                recommendVo.setOpenResultStatus(lastQs.getOpenStatus());
                recommendVo.setLastOpenReuslt(StringUtils.defaultString(lastQs.getOpenResult() , ""));
                recommendVo.setPxh(lottery.getPxh());
                recommendVo.setLotteryType(lottery.getLotteryType());
                recommendVo.setImg(allParamByMap.get("resource_domain") + lottery.getImg());
                recommendVoList.add(recommendVo);

            }, threadPoolExecutor);
            futures.add(future);
        }

        // 合并线程
        CompletableFuture[] args= new CompletableFuture[futures.size()];
        for (int i = 0; i < futures.size(); i++) {
            args[i] = futures.get(i);
        }
        CompletableFuture.allOf(args).join();

        return R.ok().put("list", recommendVoList.stream()
                .sorted(Comparator.comparing(RecommendVo::getPxh).reversed())
                .collect(Collectors.toList()));
    }
}
