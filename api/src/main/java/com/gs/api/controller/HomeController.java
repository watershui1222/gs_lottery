package com.gs.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.commons.entity.Activity;
import com.gs.commons.entity.Banner;
import com.gs.commons.entity.Notice;
import com.gs.commons.service.ActivityService;
import com.gs.commons.service.BannerService;
import com.gs.commons.service.NoticeService;
import com.gs.commons.service.SysParamService;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "首页相关", tags = "首页相关")
@RequestMapping("/home")
@RestController
public class HomeController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private ActivityService activityService;

    @ApiOperation(value = "获取活动列表")
    @GetMapping("/activity/list")
    public R activityList(HttpServletRequest httpServletRequest) {

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        List<Activity> activityList = activityService.list(
                new LambdaQueryWrapper<Activity>()
                        .eq(Activity::getStatus, 0)
                        .orderByDesc(Activity::getPxh)
        );
        JSONArray array = new JSONArray();
        for (Activity activity : activityList) {
            JSONObject object = new JSONObject();
            object.put("id", activity.getId());
            object.put("title", activity.getTitle());
            object.put("img1", allParamByMap.get("resource_domain") + activity.getImg1());
            object.put("img2", allParamByMap.get("resource_domain") + activity.getImg2());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "获取轮播图列表")
    @GetMapping("/banner/list")
    public R bannerList(HttpServletRequest httpServletRequest) {

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        List<Banner> bannerList = bannerService.list(
                new LambdaQueryWrapper<Banner>()
                        .eq(Banner::getStatus, 0)
                        .orderByDesc(Banner::getPxh)
        );
        JSONArray array = new JSONArray();
        for (Banner banner : bannerList) {
            JSONObject object = new JSONObject();
//            object.put("id", banner.getId());
            object.put("img", allParamByMap.get("resource_domain") + banner.getImg());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "获取首页通知列表")
    @GetMapping("/notice/list")
    public R noticeList(HttpServletRequest httpServletRequest) {

        List<Notice> noticeList = noticeService.list(
                new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getStatus, 0)
                        .orderByDesc(Notice::getCreateTime)
        );
        JSONArray array = new JSONArray();
        for (Notice notice : noticeList) {
            JSONObject object = new JSONObject();
            object.put("id", notice.getId());
            object.put("title", notice.getTitle());
            object.put("content", notice.getContent());
            object.put("createTime", notice.getCreateTime());
            array.add(object);
        }
        return R.ok().put("list", array);
    }

    @ApiOperation(value = "根据ID获取通知详情")
    @GetMapping("/notice/detail/{id}")
    public R noticeDetail(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        Notice notice = noticeService.getById(id);
        JSONObject object = new JSONObject();
        object.put("id", notice.getId());
        object.put("title", notice.getTitle());
        object.put("content", notice.getContent());
        object.put("createTime", notice.getCreateTime());
        return R.ok().put("data", object);
    }
}
