package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.gs.commons.utils.AesUtils;
import com.gs.commons.utils.DesUtils;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Date;
import java.util.List;

public class AGINGame {

    @Value("${platform.AG.apiDomaingi}")
    public String apiDomaingi = "https://gi.kniddk22.com";//注册 转账
    @Value("${platform.AG.apiDomaingci}")
    public String apiDomaingci = "https://gci.kniddk22.com";//获取游戏URL
    @Value("${platform.AG.cagent}")
    public String cagent = "LW2_AGIN";
    @Value("${platform.AG.md5Key}")
    public String md5Key = "lw2w9nLpJFqC";
    @Value("${platform.AG.desKey}")
    public String desKey = "lw2Fd*5E";
    @Value("${platform.AG.dataApi}")
    public String dataApi = "http://lK7Aw2.gdcapi.com:3333";

    public String checkOrCreateGameAccout(){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount1";
        String method = "lg";
        String actype = "1";
        String password = RandomUtil.randomString(8);
        String oddtype = "A";//20-50000
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("oddtype=").append(oddtype).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, true);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        System.out.println("url=" + url);
        System.out.println("调用前=" + DateUtil.now());
        HttpResponse res = null;
        try {
            res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        } catch (Exception e) {
            System.out.println("超时");
            e.printStackTrace();
        }
        System.out.println("调用后=" + DateUtil.now());
        return res.body();
    }

    public String prepareTransfreCredit(){
        String url = this.dataApi + "/getorders.xml";
        String cagent = this.cagent;
        Date mdTime = DateUtil.offsetHour(new Date(), -12);
        String enddate = DateUtil.format(mdTime,"yyyy-MM-dd HH:mm:ss");
        String startdate = DateUtil.format(DateUtil.offsetMinute(mdTime, -10),"yyyy-MM-dd HH:mm:ss");
        String by = "DESC";
        int page = 1;
        int perpage = 500;

        return "FAIL";
    }


    public void getRecord(){


    }

}
