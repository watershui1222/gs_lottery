package com.gs.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gs.api.controller.request.*;
import com.gs.api.utils.JwtUtils;
import com.gs.commons.constants.Constant;
import com.gs.commons.entity.*;
import com.gs.commons.enums.PlatSubEnum;
import com.gs.commons.service.*;
import com.gs.commons.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private TransactionRecordService transactionRecordService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private EduOrderService eduOrderService;

    @Autowired
    private KyRecordService kyRecordService;

    @Autowired
    private LyRecordService lyRecordService;


    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo userInfo = userInfoService.getUserByName(userName);
        JSONObject userObj = new JSONObject();
        userObj.put("userName", userInfo.getUserName());
        userObj.put("balance", userInfo.getBalance());
        userObj.put("nickName", userInfo.getNickName());
        userObj.put("referralCode", userInfo.getReferralCode());
        userObj.put("setPayPwdStatus", StringUtils.isNotBlank(userInfo.getPayPwd()));

        Map<String, String> paramsMap = sysParamService.getAllParamByMap();
        String resourceDomain = MapUtil.getStr(paramsMap, "resource_domain");

        Avatar avatar = avatarService.getOne(new LambdaQueryWrapper<Avatar>().eq(Avatar::getId, userInfo.getAvatarId()));
        Level level = levelService.getOne(new LambdaQueryWrapper<Level>().eq(Level::getId, userInfo.getLevelId()));
        userObj.put("avatarUrl", avatar == null ? "" : resourceDomain + avatar.getAvatarImg());
        userObj.put("levelUrl", level == null ? "" : resourceDomain + level.getLevelImg());
        userObj.put("levelName", level == null ? "" : level.getLevelName());

        return R.ok().put("user", userObj);
    }

    @ApiOperation(value = "用户余额")
    @GetMapping("/getUserBalance")
    public R getUserBalance(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo user = userInfoService.getUserByName(userName);
        return R.ok().put("balance", user.getBalance());
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
//        String captchaCode = redisTemplate.opsForValue().get("login:" + request.getCaptchaKey());
//        redisTemplate.delete(request.getCaptchaKey());
//        if (StringUtils.isBlank(captchaCode) || !captchaCode.equals(request.getCaptcha().trim().toLowerCase())) {
//            return R.error(MsgUtil.get("validation.user.register.yzmerr"));
//        }

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

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        redisTemplate.delete(RedisKeyUtil.UserTokenKey(userName));
        redisTemplate.delete(RedisKeyUtil.UserOnlineKey(userName));
        return R.ok();
    }


    @ApiOperation(value = "修改用户昵称")
    @PostMapping("/updateNickName")
    public R register(@Validated UpdateNickNameRequest request, HttpServletRequest httpServletRequest) {
        if (request.getNickName().length() > 6) {
            return R.error();
        }

        String userName = JwtUtils.getUserName(httpServletRequest);

        userInfoService.update(
                new LambdaUpdateWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, userName)
                        .set(UserInfo::getNickName, request.getNickName())
                        .set(UserInfo::getUpdateTime, new Date())
        );
        return R.ok();
    }


    @ApiOperation(value = "设置资金密码")
    @PostMapping("/setPayPwd")
    public R setPayPwd(@Validated SetPayPwdRequest request, HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo userInf = userInfoService.getUserByName(userName);
        if (StringUtils.isNotBlank(userInf.getPayPwd())) {
            return R.error();
        }
        userInfoService.update(
                new LambdaUpdateWrapper<UserInfo>()
                        .eq(UserInfo::getUserName, userName)
                        .set(UserInfo::getPayPwd, SecureUtil.md5(request.getPayPwd()))
                        .set(UserInfo::getUpdateTime, new Date())
        );
        return R.ok();
    }


    @ApiOperation(value = "修改用户密码")
    @PostMapping("/updatePwd")
    public R updatePwd(@Validated UpdatePwdRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        UserInfo user = userInfoService.getUserByName(userName);

        String oldPwd = SecureUtil.md5(request.getOldPwd());
        if (!StringUtils.equals(oldPwd, user.getLoginPwd())) {
            return R.error(MsgUtil.get("system.user.oldpwderror"));
        }

        userInfoService.update(
                new UpdateWrapper<UserInfo>().lambda()
                        .set(UserInfo::getLoginPwd, SecureUtil.md5(request.getNewPwd()))
                        .set(UserInfo::getUpdateTime, new Date())
                        .eq(UserInfo::getUserName, userName)
        );

        return R.ok();
    }


    @ApiOperation(value = "修改用户支付密码")
    @PostMapping("/updatePayPwd")
    public R updatePayPwd(@Validated UpdatePayPwdRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        UserInfo user = userInfoService.getUserByName(userName);

        String oldPwd = SecureUtil.md5(request.getOldPwd());
        if (!StringUtils.equals(oldPwd, user.getPayPwd())) {
            return R.error(MsgUtil.get("system.user.oldpwderror"));
        }

        userInfoService.update(
                new UpdateWrapper<UserInfo>().lambda()
                        .set(UserInfo::getPayPwd, SecureUtil.md5(request.getNewPwd()))
                        .set(UserInfo::getUpdateTime, new Date())
                        .eq(UserInfo::getUserName, userName)
        );
        return R.ok();
    }


    @ApiOperation(value = "用户流水记录")
    @GetMapping("/transactionRecord/list")
    public R orderList(TransactionRecordRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        params.put("type", request.getType());
        //1:今天 2:昨天 3:一周内 4:一月内
        if (StringUtils.isNotBlank(request.getDateStr())) {
            Date date = new Date();
            if (StringUtils.equals(request.getDateStr(), "2")) {
                date = DateUtil.offsetDay(date, -1);
            } else if (StringUtils.equals(request.getDateStr(), "3")) {
                date = DateUtil.offsetWeek(date, -1);
            } else if (StringUtils.equals(request.getDateStr(), "4")) {
                date = DateUtil.offsetMonth(date, -1);
            }
            Date startTime = DateUtil.beginOfDay(date);
            Date endTime = DateUtil.endOfDay(date);

            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        PageUtils page = transactionRecordService.queryPage(params);
        List<TransactionRecord> list = (List<TransactionRecord>) page.getList();

        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> businessTypeMap = new HashMap<>();
            businessTypeMap.put(0, "充值");
            businessTypeMap.put(1, "提现");
            businessTypeMap.put(2, "彩票奖金");
            businessTypeMap.put(3, "彩票撤单");
            businessTypeMap.put(4, "额度转入");
            businessTypeMap.put(5, "额度转出");
            businessTypeMap.put(6, "返水");
            businessTypeMap.put(7, "优惠活动");
            businessTypeMap.put(8, "人工上分");
            businessTypeMap.put(9, "人工下分");
            JSONArray arr = new JSONArray();
            for (TransactionRecord temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("remark", temp.getRemark());
                obj.put("time", temp.getCreateTime());
                obj.put("amount", temp.getAmount());
                obj.put("afterAmount", temp.getAfterAmount());
                obj.put("typeStr", businessTypeMap.getOrDefault(temp.getBusinessType(), "未知"));
                arr.add(obj);
            }
            page.setList(arr);
        }

        return R.ok().put("page", page);
    }


    @ApiOperation(value = "绑定银行收款账户")
    @PostMapping("/bindBankAccount")
    public R bindAccount(@Validated BindBankAccountRequest request, HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo userInf = userInfoService.getUserByName(userName);

        // 验证支付密码
        String pwd = SecureUtil.md5(request.getPayPwd());
        if (!StringUtils.equals(pwd, userInf.getPayPwd())) {
            return R.error(MsgUtil.get("system.order.paypwderror"));
        }

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        Integer accountLimit = MapUtil.getInt(allParamByMap, "account_limit", 10);
        long count = userAccountService.count(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUserName, userName));
        if (count >= accountLimit) {
            return R.error(StrUtil.format(MsgUtil.get("system.withdraw.account.limit"), accountLimit));
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUserName(userInf.getUserName());
        userAccount.setAccountType(1);
        userAccount.setChannelName(request.getBankName());
        userAccount.setAccountNo(request.getBankNo());
        userAccount.setAddress(request.getAddress());
        userAccount.setRealName(request.getPayeeName());
        userAccount.setCreateTime(new Date());
        userAccountService.save(userAccount);
        return R.ok();
    }


    @ApiOperation(value = "绑定虚拟货币账户")
    @PostMapping("/bindVirAccount")
    public R bindVirAccount(@Validated BindAccountRequest request, HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo userInf = userInfoService.getUserByName(userName);

        // 验证支付密码
        String pwd = SecureUtil.md5(request.getPayPwd());
        if (!StringUtils.equals(pwd, userInf.getPayPwd())) {
            return R.error(MsgUtil.get("system.order.paypwderror"));
        }

        Map<String, String> allParamByMap = sysParamService.getAllParamByMap();

        Integer accountLimit = MapUtil.getInt(allParamByMap, "account_limit", 10);
        long count = userAccountService.count(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUserName, userName));
        if (count >= accountLimit) {
            return R.error(StrUtil.format(MsgUtil.get("system.withdraw.account.limit"), accountLimit));
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUserName(userInf.getUserName());
        userAccount.setAccountType(4);
        userAccount.setChannelName(request.getCoinName());
        userAccount.setAccountNo(request.getAddress());
        userAccount.setAddress(request.getChannelType());
        userAccount.setRealName(null);
        userAccount.setCreateTime(new Date());
        userAccountService.save(userAccount);
        return R.ok();
    }

    @ApiOperation(value = "删除收款账户")
    @PostMapping("/delAccount")
    public R delAccount(@Validated DelAccountRequest request, HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        userAccountService.remove(
                new LambdaUpdateWrapper<UserAccount>()
                        .eq(UserAccount::getUserName, userName)
                        .eq(UserAccount::getId, request.getId()));

        return R.ok();
    }


    @ApiOperation(value = "获取用户所有收款账户")
    @GetMapping("/AllAccount")
    public R AllAccount(HttpServletRequest httpServletRequest) {

        String userName = JwtUtils.getUserName(httpServletRequest);
        List<UserAccount> list = userAccountService.list(
                new LambdaQueryWrapper<UserAccount>()
                        .eq(UserAccount::getUserName, userName)
                        .orderByDesc(UserAccount::getCreateTime)
        );

        JSONArray array = new JSONArray();
        for (UserAccount userAccount : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", userAccount.getId());
            obj.put("accountType", userAccount.getAccountType());
            obj.put("channelName", userAccount.getChannelName());
            String accountNo = userAccount.getAccountNo();
            if (accountNo.length() >= 10) {
                accountNo = accountNo.substring(0,4) + "****" + accountNo.substring(accountNo.length() - 4, accountNo.length());
//                accountNo = DesensitizedUtil.idCardNum(accountNo, 2, 4);
            }
            obj.put("accountNo", accountNo);
            obj.put("address", userAccount.getAddress());
            obj.put("realName", userAccount.getRealName());
            array.add(obj);
        }
        return R.ok().put("list", array);
    }


    @Transactional
    @ApiOperation(value = "用户提现")
    @PostMapping("/withdraw")
    public R withdraw(@Validated WithdrawRequest request, HttpServletRequest httpServletRequest) throws Exception {

        String userName = JwtUtils.getUserName(httpServletRequest);
        UserInfo userInf = userInfoService.getUserByName(userName);

        Date now = new Date();
        Map<String, String> params = sysParamService.getAllParamByMap();
        // 验证时间段
        String withdrawTimeStr = params.get("withdraw_time");
        if (StringUtils.isNotBlank(withdrawTimeStr)) {
            String today = DateUtil.formatDate(now);
            String[] timeArr = withdrawTimeStr.split("-");
            Date beginTime = DateUtil.parse(today + " " + timeArr[0]);
            Date endTime = DateUtil.parse(today + " " + timeArr[1]);
            if (!DateUtil.isIn(now, beginTime, endTime)) {
                return R.error(MsgUtil.get("system.withdraw.time") + ":" + withdrawTimeStr);
            }
        }

        // 验证支付密码
        String pwd = SecureUtil.md5(request.getPayPwd());
        if (!StringUtils.equals(pwd, userInf.getPayPwd())) {
            return R.error(MsgUtil.get("system.order.paypwderror"));
        }

        // 验证资金冻结
        if (userInf.getPayStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.funds.enable"));
        }

        // 查询是否还有待审核的订单
        long noFinish = withdrawService.count(
                new LambdaQueryWrapper<Withdraw>()
                        .eq(Withdraw::getUserName, userName)
                        .eq(Withdraw::getStatus, 0)
        );
        if (noFinish > 0) {
            return R.error(MsgUtil.get("system.withdraw.hasorder"));
        }


        BigDecimal amount = new BigDecimal(request.getAmount());
        // 校验余额是否充足
        if (amount.doubleValue() > userInf.getBalance().doubleValue()) {
            return R.error(MsgUtil.get("system.order.balance"));
        }
        // 查询提现账户信息
        UserAccount userAccount = userAccountService.getOne(
                new LambdaQueryWrapper<UserAccount>()
                        .eq(UserAccount::getId, request.getAccountId())
                        .eq(UserAccount::getUserName, userName)
        );
        if (userAccount == null) {
            return R.error(MsgUtil.get("system.withdraw.noaccount"));
        }

        // 扣除用户金额
        userInfoService.updateUserBalance(userName, amount.negate());

        // 提现记录
        String withdrawOrderNo = IdUtils.getWithdrawOrderNo();
        Withdraw withdraw = new Withdraw();
        withdraw.setUserName(userName);
        withdraw.setOrderNo(withdrawOrderNo);
        withdraw.setAmount(amount);
        withdraw.setCreateTime(now);
        withdraw.setCheckTime(null);
        withdraw.setStatus(0);
        withdraw.setRemark(null);
        withdraw.setOperName(null);
        withdraw.setUpdateTime(null);
        withdraw.setAccountType(userAccount.getAccountType());
        String accountDetail = "";
        if (userAccount.getAccountType().intValue() == 1) {
            accountDetail = StrUtil.format("银行名称:{}|银行卡号:{}|持卡人:{}|开户网点:{}", userAccount.getChannelName(), userAccount.getAccountNo(), userAccount.getRealName(), userAccount.getAddress());
        } else if (userAccount.getAccountType().intValue() == 4) {
            accountDetail = StrUtil.format("币种:{}|网络:{}|地址:{}", userAccount.getChannelName(), userAccount.getAddress(), userAccount.getAccountNo());
        }
        withdraw.setAccountDetail(accountDetail);
        withdrawService.save(withdraw);
        // 资金流水记录
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setUserName(userName);
        transactionRecord.setAmount(amount.negate());
        transactionRecord.setBeforeAmount(userInf.getBalance());
        transactionRecord.setAfterAmount(NumberUtil.sub(userInf.getBalance(), amount));
        transactionRecord.setPayType(1);
        transactionRecord.setBusinessType(1);
        transactionRecord.setBusinessOrder(withdrawOrderNo);
        transactionRecord.setCreateTime(now);
        transactionRecord.setRemark("用户提现" + amount + "元");
        transactionRecord.setOperName(null);
        transactionRecordService.save(transactionRecord);
        return R.ok(MsgUtil.get("system.withdraw.success"));
    }

    @ApiOperation(value = "用户提现记录")
    @GetMapping("/withdraw/record")
    public R withdrawRecord(@Validated PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        PageUtils page = withdrawService.queryPage(params);
        List<Withdraw> list = (List<Withdraw>) page.getList();

        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> businessTypeMap = new HashMap<>();
            businessTypeMap.put(0, "充值");
            JSONArray arr = new JSONArray();
            for (Withdraw temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("time", temp.getCreateTime());
                obj.put("amount", temp.getAmount());
                obj.put("checkTime", temp.getCheckTime());
                obj.put("status", temp.getStatus());
                obj.put("remark", temp.getRemark());
                obj.put("orderId", temp.getOrderNo());
                obj.put("accountDetail", temp.getAccountDetail());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "获取返水金额")
    @GetMapping("/getReturnAmount")
    public R getReturnAmount(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        Date now = new Date();
        // 所有返水key
        DateTime yesterday = DateUtil.offsetDay(now, -1);
        String autoKey = RedisKeyUtil.AutoReturn(userName, yesterday);
        Boolean flag = redisTemplate.hasKey(autoKey);
        // TODO: 2024/4/5 查询返回金额
        return R.ok().put("data", RandomUtil.randomInt(1, 100)).put("flag", flag);
    }

    @Transactional
    @ApiOperation(value = "领取自助返水")
    @GetMapping("/autoReturn")
    public R autoReturn(HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 校验是否领取昨日金额
        Date now = new Date();
        // 所有返水key
        DateTime yesterday = DateUtil.offsetDay(now, -1);
        String autoKey = RedisKeyUtil.AutoReturn(userName, yesterday);
        if (redisTemplate.hasKey(autoKey)) {
            return R.error(MsgUtil.get("system.autoreturn.has"));
        }

        UserInfo user = userInfoService.getUserByName(userName);

        // TODO: 2024/4/5 计算返水金额业务
        // 计算返水金额
        BigDecimal amount = new BigDecimal(RandomUtil.randomInt(1, 100));
        
        // 给用户加钱
        userInfoService.updateUserBalance(userName, amount);
        // 添加流水记录
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setUserName(userName);
        transactionRecord.setAmount(amount);
        transactionRecord.setBeforeAmount(user.getBalance());
        transactionRecord.setAfterAmount(NumberUtil.add(amount, user.getBalance()));
        transactionRecord.setPayType(0);
        transactionRecord.setBusinessType(7);
        transactionRecord.setBusinessOrder(null);
        transactionRecord.setCreateTime(now);
        transactionRecord.setRemark("领取"+ DateUtil.formatDate(yesterday) +"返水：" + amount + "元");
        transactionRecord.setOperName(null);
        transactionRecordService.save(transactionRecord);
        
        redisTemplate.opsForValue().set(autoKey, "true", 2, TimeUnit.DAYS);
        return R.ok();
    }

    @ApiOperation(value = "第三方游戏记录")
    @GetMapping("/platOrder/list")
    public R platOrderList(PlatOrderListRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        //1:今天 2:昨天 3:一周内 4:一月内
        if (StringUtils.isNotBlank(request.getDateStr())) {
            Date date = new Date();
            if (StringUtils.equals(request.getDateStr(), "2")) {
                date = DateUtil.offsetDay(date, -1);
            } else if (StringUtils.equals(request.getDateStr(), "3")) {
                date = DateUtil.offsetWeek(date, -1);
            } else if (StringUtils.equals(request.getDateStr(), "4")) {
                date = DateUtil.offsetMonth(date, -1);
            }
            Date startTime = DateUtil.beginOfDay(date);
            Date endTime = DateUtil.endOfDay(date);

            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }

        PageUtils pageUtils;
        if (StringUtils.equals(PlatSubEnum.KY.getPlatSubCode(), request.getSubPlatCode())) {
            pageUtils = kyRecordService.queryPage(params);
        } else if (StringUtils.equals(PlatSubEnum.LY.getPlatSubCode(), request.getSubPlatCode())) {
            pageUtils = lyRecordService.queryPage(params);
        } else {
            return R.error("未查到对应游戏厅方");
        }

        return R.ok().put("page", pageUtils);
    }



    @ApiOperation(value = "额度转换记录")
    @GetMapping("/eduOrder/list")
    public R eduOrderList(EduOrderListRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        params.put("platCode", request.getPlatCode());
        //1:今天 2:昨天 3:一周内 4:一月内
        if (StringUtils.isNotBlank(request.getDateStr())) {
            Date date = new Date();
            if (StringUtils.equals(request.getDateStr(), "2")) {
                date = DateUtil.offsetDay(date, -1);
            } else if (StringUtils.equals(request.getDateStr(), "3")) {
                date = DateUtil.offsetWeek(date, 1);
            } else if (StringUtils.equals(request.getDateStr(), "4")) {
                date = DateUtil.offsetMonth(date, 1);
            }
            Date startTime = DateUtil.beginOfDay(date);
            Date endTime = DateUtil.endOfDay(date);

            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }

        PageUtils page = eduOrderService.queryPage(params);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<EduOrder> pageList = (List<EduOrder>) page.getList();
            JSONArray jsonArray = new JSONArray();
            for (EduOrder eduOrder : pageList) {
                JSONObject jsonObject = new JSONObject();
                String transPlatStr = eduOrder.getEduType() == 1
                        ? StrUtil.format("{}->{}", "平台", eduOrder.getPlatCode())
                        : StrUtil.format("{}->{}", eduOrder.getPlatCode(), "平台");

                jsonObject.put("transPlatStr", transPlatStr);
                jsonObject.put("time", eduOrder.getCreateTime());
                jsonObject.put("amount", eduOrder.getAmount());
                jsonObject.put("status", eduOrder.getStatus());
                jsonObject.put("type", eduOrder.getEduType());
                jsonArray.add(jsonObject);

            }
            page.setList(jsonArray);
        }

        return R.ok().put("page", page);
    }
}
