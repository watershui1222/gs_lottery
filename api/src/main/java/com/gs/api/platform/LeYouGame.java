package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.gs.api.platform.platUtils.KaiYuanUtil;

import java.util.Date;

/**
 * 乐游
 */
public class LeYouGame {

    public String apiDomain = "https://wc1-api.kewmn686.com/channelHandle";
    public String agent = "201373";
    public String aesKey = "4686951BAA2E5234";
    public String md5Key = "D08C49D1629ECF26";
    public String betRecordDomain = "https://wc1-record.kewmn686.com/getRecordHandle";

    /**
     * 登录
     * @return
     * @throws Exception
     */
    public String login() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GSLY";
        String kindId = "220";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=0&")
                .append("account=").append(account)
                .append("&money=0")
                .append("&orderid=").append(orderid)
                .append("&ip=127.0.0.1")
                .append("&lineCode=").append(lineCode)
                .append("&KindID=").append(kindId);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        return result;
    }


    public static void main(String[] args) throws Exception {
        LeYouGame ly = new LeYouGame();
        System.out.println(ly.login());
    }

}
