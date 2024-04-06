package com.gs.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.api.controller.VO.LotteryHandicapVo;
import com.gs.api.controller.VO.LotteryPlayVo;
import com.gs.api.controller.request.LotteryOrderListRequest;
import com.gs.api.controller.request.LotteryTimeRequest;
import com.gs.api.controller.request.OpenResultHistoryRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.commons.bo.OpenresultTimeBO;
import com.gs.commons.constants.Constant;
import com.gs.commons.entity.*;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.*;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private LotteryOrderService lotteryOrderService;


    @ApiOperation(value = "获取指定彩种下的所有盘口以及玩法")
    @GetMapping("/getAllPlay/{lotteryCode}")
    public R getAllPlay(@PathVariable("lotteryCode") String lotteryCode) {
        // 获取彩种信息
        Lottery lottery = lotteryService.getOne(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getLotteryCode, lotteryCode)
        );

        // 从缓存读取 缓存没有去数据库读取
        String redisKey = "lottery:plays:" + lottery;
//        String redisValue = redisTemplate.opsForValue().get(redisKey);
//        if (StringUtils.isNotBlank(redisValue)) {
//            List<LotteryHandicapVo> lotteryHandicapVoList = JSONArray.parseArray(redisValue, LotteryHandicapVo.class);
//            return R.ok().put("plays", lotteryHandicapVoList).put("lotteryType", lottery.getLotteryType());
//        }

        // 获取彩种下的所有盘口
        List<LotteryHandicap> handicaps = lotteryHandicapService.list(
                new LambdaQueryWrapper<LotteryHandicap>()
                        .eq(LotteryHandicap::getLotteryCode, lottery.getLotteryCode())
                        .eq(LotteryHandicap::getStatus, 0)
                        .orderByDesc(LotteryHandicap::getPxh)
        );
        // 获取彩种下的所有玩法
        List<LotteryPlay> plays = lotteryPlayService.list(
                new LambdaQueryWrapper<LotteryPlay>()
                        .eq(LotteryPlay::getLotteryCode, lottery.getLotteryCode())
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
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(lotteryHandicapVoList), 1, TimeUnit.HOURS);
        return R.ok().put("plays", lotteryHandicapVoList).put("lotteryType", lottery.getLotteryType());
    }


    @ApiOperation(value = "获取指定彩种下所有玩法的号码以及赔率")
    @GetMapping("/getAllOdds/{lotteryCode}/{playCode}")
    public R getAllPlay(@PathVariable("lotteryCode") String lotteryCode, @PathVariable("playCode") String playCode) {
        // 获取彩种信息
        Lottery lottery = lotteryService.getOne(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getLotteryCode, lotteryCode)
        );

        List<LotteryOdds> oddsList = lotteryOddsService.list(
                new LambdaQueryWrapper<LotteryOdds>()
                        .eq(LotteryOdds::getLotteryCode, lotteryCode)
                        .eq(LotteryOdds::getPlayCode, playCode)
                        .eq(LotteryOdds::getStatus, 0)
                        .orderByDesc(LotteryOdds::getPxh)
        );

        JSONArray jsonArray = new JSONArray();
        for (LotteryOdds lotteryOdds : oddsList) {
            JSONObject object = new JSONObject();
            object.put("code", lotteryOdds.getHmCode());
            object.put("name", lotteryOdds.getHmName());
            object.put("odds", lotteryOdds.getOdds());
            object.put("g", lotteryOdds.getGroupName());
            jsonArray.add(object);
        }

        return R.ok().put("odds", jsonArray);
    }




    @ApiOperation(value = "历史开奖")
    @GetMapping("/openHistory/list")
    public R openHistoryList(OpenResultHistoryRequest request, HttpServletRequest httpServletRequest) {
        Lottery lottery = lotteryService.getOne(Wrappers.lambdaQuery(Lottery.class).eq(Lottery::getLotteryCode, request.getLotteryCode()).eq(Lottery::getStatus, 0));
        if (null == lottery) {
            return R.error("该彩种不存在或已停用");
        }

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("nowTime", DateUtil.date());

        PageUtils pageUtils;
        if (StringUtils.equals(LotteryCodeEnum.BJKL8.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultBjkl8Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.BJPK10.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultBjpk10Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.CQSSC.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultCqsscService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.FC3D.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultFc3dService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.FT.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultFtService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.GD11X5.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultGd11x5Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.JSK3.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultJsk3Service.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.MO6HC.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultMo6hcService.queryPage(params);

        } else if (StringUtils.equals(LotteryCodeEnum.PCDD.getLotteryCode(), request.getLotteryCode())) {
            pageUtils = openresultPcddService.queryPage(params);

        } else {
            return R.error("未查询到对应彩种");
        }


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

        PageUtils page = lotteryOrderService.queryPage(params);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<LotteryOrder> pageList = (List<LotteryOrder>) page.getList();
            JSONArray jsonArray = new JSONArray();
            for (LotteryOrder lotteryOrder : pageList) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("lotteryName", lotteryOrder.getLotteryName());
                jsonObject.put("playName", lotteryOrder.getPlayName());
                jsonObject.put("qs", lotteryOrder.getQs());
                jsonObject.put("betAmount", lotteryOrder.getBetAmount());
                jsonObject.put("betTime", lotteryOrder.getBetTime());
                jsonObject.put("bonusAmount", lotteryOrder.getBonusAmount());
                jsonObject.put("openResult", lotteryOrder.getOpenResult());
                jsonObject.put("orderStatus", lotteryOrder.getOrderStatus());
                jsonArray.add(jsonObject);

            }
            page.setList(jsonArray);
        }

        return R.ok().put("page", page);
    }


    @ApiOperation(value = "获取当前期和上期开奖")
    @GetMapping("/lotteryQsTime")
    public R lotteryTime(@Validated LotteryTimeRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        Date now = new Date();
        OpenresultTimeBO currentQsData;
        OpenresultTimeBO lastQsData;
        if (StringUtils.equals(LotteryCodeEnum.BJKL8.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultBjkl8Service.getOneDataByTime(now, null);
            lastQsData = openresultBjkl8Service.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.BJPK10.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultBjpk10Service.getOneDataByTime(now, null);
            lastQsData = openresultBjpk10Service.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.CQSSC.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultCqsscService.getOneDataByTime(now, null);
            lastQsData = openresultCqsscService.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.FC3D.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultFc3dService.getOneDataByTime(now, null);
            lastQsData = openresultFc3dService.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.FT.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultFtService.getOneDataByTime(now, null);
            lastQsData = openresultFtService.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.GD11X5.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultGd11x5Service.getOneDataByTime(now, null);
            lastQsData = openresultGd11x5Service.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.JSK3.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultJsk3Service.getOneDataByTime(now, null);
            lastQsData = openresultJsk3Service.getOneDataByTime(null, now);

        } else if (StringUtils.equals(LotteryCodeEnum.MO6HC.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultMo6hcService.getOneDataByTime(now, null);
            lastQsData = openresultMo6hcService.getOneDataByTime(null, now);

        }else if (StringUtils.equals(LotteryCodeEnum.PCDD.getLotteryCode(), request.getLotteryCode())) {
            currentQsData = openresultPcddService.getOneDataByTime(now, null);
            lastQsData = openresultPcddService.getOneDataByTime(null, now);
        } else {
            return R.error("未查询到该彩种");
        }
        // 当前期
        JSONObject nowQsJson = new JSONObject();
        String nowQs = (null == currentQsData) ? "" : currentQsData.getQs();


        long nowCloseSeconds = (null == currentQsData) ? -1L : (currentQsData.getCloseTime().getTime() - now.getTime()) / 1000L;
        long nowOpenSeconds = (null == currentQsData) ? -1L : (currentQsData.getOpenResultTime().getTime() - now.getTime()) / 1000L;
        nowQsJson.put("qs", nowQs);
        nowQsJson.put("closeSeconds", nowCloseSeconds);
        nowQsJson.put("openSeconds", nowOpenSeconds);

        // 上一期
        JSONObject lastQsJson = new JSONObject();
        String lastQs = (null == lastQsData) ? "" : lastQsData.getQs();
        String openResult = (null == lastQsData) ? "" : StringUtils.defaultString(lastQsData.getOpenResult());
        lastQsJson.put("qs", lastQs);
        lastQsJson.put("openResult", openResult);

        return R.ok().put("nowQs", nowQsJson).put("lastQs", lastQsJson);
    }

}
