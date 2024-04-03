package com.gs.api.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.api.utils.JwtUtils;
import com.gs.commons.entity.Avatar;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.AvatarService;
import com.gs.commons.service.SysParamService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "头像相关", tags = "头像相关")
@RequestMapping("/avatar")
@RestController
public class AvatarController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private AvatarService avatarService;

    @ApiOperation(value = "获取所有用户头像")
    @GetMapping("/list")
    public R list() {
        List<Avatar> avatars = avatarService.list(
                new LambdaQueryWrapper<Avatar>()
                        .eq(Avatar::getStatus, 0)
                        .orderByDesc(Avatar::getPxh));

        Map<String, String> paramsMap = sysParamService.getAllParamByMap();
        String resourceDomain = MapUtil.getStr(paramsMap, "resource_domain");

        JSONArray array = new JSONArray();
        for (Avatar avatar : avatars) {
            JSONObject obj = new JSONObject();
            obj.put("avatarId", avatar.getId());
            obj.put("avatarUrl", resourceDomain + avatar.getAvatarImg());
            array.add(obj);
        }

        return R.ok().put("list", array);
    }

    @ApiOperation(value = "修改用户头像")
    @GetMapping("/set/{id}")
    public R setUserAvatar(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Avatar avatar = avatarService.getById(id);
        if (avatar == null) {
            return R.error();
        }

        userInfoService.update(
                new LambdaUpdateWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, userName)
                        .set(UserInfo::getAvatarId, avatar.getId())
                        .set(UserInfo::getUpdateTime, new Date())
        );
        return R.ok();
    }
}
