package com.gs.business.service.impl.plat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class SbServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.ShaBa.operatorID}")
    public String operatorID;
    @Value("${platform.ShaBa.apiUrl}")
    public String apiUrl;
    @Value("${platform.ShaBa.vendorID}")
    public String vendorID;
    @Value("${platform.ShaBa.currencyID}")
    public String currencyID;//20=测试货币 13=CNY

    @Autowired
    private UserPlatService userPlatService;

    @Override
    public UserPlat register(String userName) throws Exception {
        // 查询是否注册平台
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, "SB")
        );
        if (userPlat != null) {
            return userPlat;
        }

        // 注册三方
        String apiUrlStr = this.apiUrl + "/CreateMember";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = this.owner + userName;
        String operatorId = this.operatorID;
        String username = vendor_member_id;
        int oddstype = 2;
        int currency = Integer.valueOf(this.currencyID);
        BigDecimal mintransfer = BigDecimal.ZERO;
        BigDecimal maxtransfer = new BigDecimal("100000000");
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("operatorId", operatorId);
        param.put("username", username);
        param.put("oddstype", oddstype);
        param.put("currency", currency);
        param.put("mintransfer", mintransfer);
        param.put("maxtransfer", maxtransfer);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 createMember param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") != 0){
            log.error("沙巴 createMember param= " + param + " result = " + result);
            throw new Exception("沙巴注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode("SB");
        save.setPlatUserName(vendor_member_id);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String apiUrlStr = this.apiUrl + "/CheckUserBalance";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_ids = userPlat.getPlatUserName();
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_ids", vendor_member_ids);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 getBalance result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            JSONArray data = resJSON.getJSONArray("Data");
            if(CollUtil.isNotEmpty(data)){
                JSONObject obj = (JSONObject) data.get(0);
                BigDecimal balance = obj.getBigDecimal("balance");
                return balance == null ? BigDecimal.ZERO : balance;
            }
        }
        log.error("沙巴 getBalance 失败 param= " + param + " result = " + result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String apiUrlStr = this.apiUrl + "/GetSabaUrl";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = userPlat.getPlatUserName();
        int platform = userPlat.getDevice();//1: 桌机pc  2: 手机 h5
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("platform", platform);
        String result = HttpUtil.post(apiUrlStr, param);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") != 0){
            log.error("沙巴 getLoginUrl失败 param= " + param + " result = " + result);
            throw new Exception("沙巴登录失败");
        }
        //成功
        String data = resJSON.getString("Data");
        String url = data + "&lang=cs&OType=2";//cs=中文  OType : 1=欧洲盘 2=中国盘 3=印尼盘 4=马来盘 5=美国盘
        return url;
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        String apiUrlStr = this.apiUrl + "/FundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = userPlat.getPlatUserName();
        String vendor_trans_id = platOrderNo;//我方单号
        BigDecimal amount = money;
        int currency = Integer.valueOf(this.currencyID);
        int direction = 1;//0:转出  1:转入
        int wallet_id = 1;//体育
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("amount", amount);
        param.put("currency", currency);
        param.put("direction", direction);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 transferIn param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            //成功
            return true;
        }else if(error_code == 1){
            //需确认 status code  1失败  2 代表状态未知，请呼叫 API“CheckFundTransfer”方法确认状态
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 1){
                return false;
            }else if(status == 2){
                //需确认
                throw new Exception("沙巴转入异常");
            }
        }
        return false;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        String apiUrlStr = this.apiUrl + "/FundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = userPlat.getPlatUserName();
        String vendor_trans_id = platOrderNo;//我方单号
        int currency = Integer.valueOf(this.currencyID);
        int direction = 0;
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("amount", amount);
        param.put("currency", currency);
        param.put("direction", direction);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 transferOut param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            //成功
            return true;
        }else if(error_code == 1){
            //需确认 status code  1失败  2 代表状态未知，请呼叫 API“CheckFundTransfer”方法确认状态
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 1){
                return false;
            }else if(status == 2){
                //需确认
                throw new Exception("沙巴转出异常");
            }
        }
        return false;
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return userPlat.getPlatUserName() + "INSABA" + RandomUtil.randomString(7);
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return userPlat.getPlatUserName() + "OUTSABA" + RandomUtil.randomString(7);
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String apiUrlStr = this.apiUrl + "/KickUser";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = userPlat.getPlatUserName();
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 logout param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        return error_code == 0;
    }
}
