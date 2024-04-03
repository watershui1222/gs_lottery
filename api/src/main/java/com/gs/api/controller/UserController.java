package com.gs.api.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gs.api.controller.request.CheckExistRequest;
import com.gs.api.controller.request.LoginRequest;
import com.gs.api.controller.request.UserRegisterRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.commons.entity.Avatar;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.entity.UserLoginLog;
import com.gs.commons.service.*;
import com.gs.commons.utils.MsgUtil;
import com.gs.commons.utils.R;
import com.gs.commons.utils.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {
    @Value("${token.expire}")
    private int expire;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private UserLoginLogService userLoginLogService;

    @Autowired
    private LevelService levelService;

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        return R.ok().put("list", userInfoService.getById(1));
    }

    @ApiOperation(value = "检测当前用户名是否存在")
    @PostMapping("/checkExist")
    public R checkExist(@Validated CheckExistRequest request) {
        // 查询用户名是否存在
        long exist = userInfoService.count(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, request.getUserName())
        );
        return R.ok().put("exist", exist > 0);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R register(@Validated UserRegisterRequest request, HttpServletRequest httpServletRequest) throws Exception {
        // 获取验证码
        String captchaCode = redisTemplate.opsForValue().get("register:" + request.getCaptchaKey());
        redisTemplate.delete(request.getCaptchaKey());
        if (StringUtils.isBlank(captchaCode) || !captchaCode.equals(request.getCaptcha().trim().toLowerCase())) {
            return R.error(MsgUtil.get("validation.user.register.yzmerr"));
        }

        // 查询用户名是否存在
        long exist = userInfoService.count(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, request.getUserName())
        );
        if (exist > 0) {
            return R.error(MsgUtil.get("system.user.register.exist"));
        }

        // 查询代理用户
        UserInfo agent = userInfoService.getOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getReferralCode, request.getInviteCode())
        );
        if (agent == null) {
            return R.error(MsgUtil.get("system.user.register.invitecode"));
        }

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        Date now = new Date();
        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");

        // 查询IP注册量
        long count = userInfoService.count(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getRegisterIp, clientIP));
        Integer ipzc = MapUtil.getInt(allParamByMap, "register_same_ip", 5);
        if (count >= ipzc) {
            log.error("同IP["+ clientIP +"]注册达到" + count + "个");
            return R.error();
        }

        // 获取头像列表
        List<Avatar> avatars = avatarService.list(
                new LambdaQueryWrapper<Avatar>()
                        .eq(Avatar::getStatus, 0)
        );
        Collections.shuffle(avatars);

        UserInfo user = new UserInfo();
        user.setUserName(request.getUserName());
        user.setNickName(null);
        user.setRealName(null);
        user.setBalance(new BigDecimal("0"));
        user.setYebBalance(new BigDecimal("0"));
        user.setYebInterest(new BigDecimal("0"));
        user.setLoginPwd(SecureUtil.md5(request.getLoginPwd()));
        user.setPayPwd(null);
        user.setLoginStatus(0);
        user.setPayStatus(0);
        user.setUserPhone(null);
        user.setUserAgent(agent.getUserName());
        user.setReferralCode(RandomUtil.randomNumbers(6));
        user.setAvatarId(avatars.get(0).getId());
        user.setLevelId(1);
        user.setGroupId(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setLastTime(now);
        user.setLastIp(clientIP);
        user.setRemark(null);
        user.setRegisterIp(clientIP);
        userInfoService.save(user);

        // 登录日志
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserName(request.getUserName());
        loginLog.setLoginIp(clientIP);
        loginLog.setAddrDetail(null);
        loginLog.setLoginDomain(null);
        loginLog.setCreateTime(now);
        userLoginLogService.save(loginLog);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserOnlineKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        return R.ok().put("token", token);
    }


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@Validated LoginRequest request, HttpServletRequest httpServletRequest) {
        // 获取验证码
        String captchaCode = redisTemplate.opsForValue().get("login:" + request.getCaptchaKey());
        redisTemplate.delete(request.getCaptchaKey());
        if (StringUtils.isBlank(captchaCode) || !captchaCode.equals(request.getCaptcha().trim().toLowerCase())) {
            return R.error(MsgUtil.get("validation.user.register.yzmerr"));
        }

        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");

        // 查询用户信息
        UserInfo user = userInfoService.getUserByName(request.getUserName());
        if (user == null) {
            return R.error(MsgUtil.get("system.user.login.noexist"));
        }

        if (user.getLoginStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }

        // 获取所有参数配置
        Map<String, String> paramsMap = sysParamService.getAllParamByMap();
        String incKey = RedisKeyUtil.LoginPwdErrorKey(user.getUserName());
        /** 每日错误次数上限 **/
        String dayCount = redisTemplate.opsForValue().get(incKey);
        int count = NumberUtils.toInt(dayCount, 0);
        Integer pwdErrCount = MapUtil.getInt(paramsMap, "login_pwd_error", 0);
        if (pwdErrCount > 0 && count >= pwdErrCount) {
            return R.error(MsgUtil.get("system.user.login.pwd.limit"));
        }

        // 验证密码正确
        String pwd = SecureUtil.md5(request.getLoginPwd());
        if (!StringUtils.equals(pwd, user.getLoginPwd())) {
            /** 累计密码错误 **/
            redisTemplate.opsForValue().increment(incKey);
            redisTemplate.expire(incKey, 10, TimeUnit.MINUTES);
            return R.error(MsgUtil.get("system.login.pwderror"));
        }

        Date now = new Date();
        /** 更新最后登录时间 **/
        userInfoService.update(
                new UpdateWrapper<UserInfo>().lambda()
                        .eq(UserInfo::getId, user.getId())
                        .set(UserInfo::getLastIp, clientIP)
                        .set(UserInfo::getLastTime, now)
        );

        // 登录日志
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserName(request.getUserName());
        loginLog.setLoginIp(clientIP);
        loginLog.setAddrDetail(null);
        loginLog.setLoginDomain(null);
        loginLog.setCreateTime(now);
        userLoginLogService.save(loginLog);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, expire, TimeUnit.MINUTES);
        // 续期在线用户
        redisTemplate.opsForValue().set(RedisKeyUtil.UserOnlineKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);
        return R.ok().put("token", token);
    }
}
