package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.gs.commons.utils.DesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.math.BigDecimal;
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
    @Value("${platform.AG.dataMd5}")
    public String dataMd5 = "676320F3F5E70FB7BA110990FD8BD45C";
    @Value("${platform.AG.dataAgent}")
    public String dataAgent = "LW2";

    public String checkOrCreateGameAccout(){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount2";
        String method = "lg";
        String actype = "1";
        String password = "testpassword111";
        String oddtype = "A";//20-50000
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("oddtype=").append(oddtype).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info, "0")){
            return "OK";
        }
        return "FAIL";
    }

    public BigDecimal getBalance(){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount2";
        String method = "gb";
        String actype = "1";
        String password = "testpassword111";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(NumberUtil.isNumber(info)){
            return new BigDecimal(info);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 预备转账
     * @param type  IN  OUT
     * @return
     */
    private String prepareTransfreCredit(String type){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount2";
        String method = "tc";
        String billno = cagent + "2568954782149";
        BigDecimal credit = new BigDecimal("1000");
        String actype = "1";
        String password = "testpassword111";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("credit=").append(credit).append("/\\\\\\\\/")
                .append("type=").append(type).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        System.out.println("预备转账:"+res.body());
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return "1";
        }
        return "0";
    }

    /**
     * 转账确认
     * @param type  IN  OUT
     * @return
     */
    private boolean transferCreditConfirm(String type, String flag){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount2";
        String method = "tcc";
        String billno = cagent + "2568954782149";
        BigDecimal credit = new BigDecimal("1000");
        String actype = "1";
        String password = "testpassword111";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("credit=").append(credit).append("/\\\\\\\\/")
                .append("type=").append(type).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("flag=").append(flag).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        System.out.println("转账确认:"+res.body());
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return true;
        }
        return false;
    }

    /**
     * 查询订单状态
     * @param
     * @return
     */
    private boolean queryOrderStatus(){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String billno = cagent + "2568954782149";
        String method = "qos";
        String actype = "1";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        System.out.println("转账查询:"+res.body());
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return true;
        }
        return false;
    }

    /**
     * 转入
     * @return
     */
    public boolean transferIn(){
        //先调用预备转账
        String flag = prepareTransfreCredit("IN");
        //确认转账
        boolean result1 = transferCreditConfirm("IN", flag);
        //查询订单
        boolean result2 = queryOrderStatus();
        return result1 && result2;
    }

    /**
     * 转出
     * @return
     */
    public boolean transferOut(){
        //先调用预备转账
        String flag = prepareTransfreCredit("OUT");
        //确认转账
        boolean result1 = transferCreditConfirm("OUT", flag);
        //查询订单
        boolean result2 = queryOrderStatus();
        return result1 && result2;
    }

    /**
     * 获取游戏记录
     * @param
     * @return
     */
    private String forwardGame(){
        String apiDomain = this.apiDomaingci + "/forwardGame.do";
        String cagent = this.cagent;
        String loginname = "gstestaccount2";
        String actype = "1";
        String password = "testpassword111";
        String dm = "NO_RETURN";
        String sid = cagent + "3584589654139";
        String lang = "1";
        String gameType = "HM3D";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("dm=").append(dm).append("/\\\\\\\\/")
                .append("sid=").append(sid).append("/\\\\\\\\/")
                .append("lang=").append(lang).append("/\\\\\\\\/")
                .append("gameType=").append(gameType).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        return url;
    }

    private String getResultInfo(String xmlStr){
        Document document = XmlUtil.parseXml(xmlStr);
        Element resultEl = XmlUtil.getRootElement(document);
        String info = resultEl.getAttribute("info");
        return info;
    }

    private String getTextBytagName(String xmlStr){
        Document document = XmlUtil.parseXml(xmlStr);
        Element resultEl = XmlUtil.getRootElement(document);
        String info = XmlUtil.getElement(resultEl, "info").getTextContent();
        return info;
    }


    public void getRecord(String subCode){
        String method = "";
        if(StrUtil.equals(subCode, "AGLIVE")){
            method = "getorders.xml";
        }else if(StrUtil.equals(subCode, "AGFISH")){
            method = "gethunterorders.xml";
        }else if (StrUtil.equals(subCode, "XIN")){
            method = "getslotorders_ex.xml";
        }else if(StrUtil.equals(subCode, "YOPLAY")){
            method = "getyoplayorders_ex.xml";
        }
        String domain = this.dataApi + "/" + method;
        String cagent = this.dataAgent;
        Date mdTime = DateUtil.offsetHour(new Date(), -12);
        String enddate = DateUtil.format(mdTime,"yyyy-MM-dd HH:mm:ss");
        String startdate = DateUtil.format(DateUtil.offsetMinute(mdTime, -10),"yyyy-MM-dd HH:mm:ss");
        if(StrUtil.equals(subCode, "AGFISH")){
            enddate = DateUtil.current()/1000 + "";
            startdate = DateUtil.offsetMinute(new Date(), -10).getTime()/1000 + "";
        }
        String by = "DESC";
        int page = 1;
        int perpage = 500;
        int totalpage = 1;
        String key = DesUtils.MD5(this.dataAgent + startdate + enddate + by + page + perpage + this.dataMd5);

        do{
            StringBuilder paramSB = new StringBuilder();
            paramSB.append("?cagent=").append(cagent)
                    .append("&startdate=").append(startdate)
                    .append("&enddate=").append(enddate)
                    .append("&by=").append(by)
                    .append("&page=").append(page)
                    .append("&perpage=").append(perpage)
                    .append("&key=").append(key);
            String url = domain + paramSB;
            String result = HttpUtil.get(url);
            System.out.println(url);
            System.out.println("result=" + result);
            Document document = XmlUtil.parseXml(result);
            Element resultEl = XmlUtil.getRootElement(document);
            String info = XmlUtil.getElement(resultEl, "info").getTextContent();
            if(StrUtil.equals(info, "0")){
                Element addition = XmlUtil.getElement(resultEl, "addition");
                totalpage = Integer.valueOf(XmlUtil.getElement(addition, "totalpage").getTextContent());
                List<Element> row = XmlUtil.getElements(resultEl, "row");
                for (Element e:row){

                }
            }
            page++;
        }while(page <= totalpage);
    }

//    public static void main(String[] args) {
//        AGINGame ag = new AGINGame();
//        System.out.println(ag.forwardGame());
//        ag.getRecord("AGFISH");
//    }

}
