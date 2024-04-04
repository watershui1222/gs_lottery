package com.gs.task.schedule;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultJsk3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class OpenSchedule {

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    private static final String DUOCAI_URL = "https://vip.manycai.com/K2653601ec5af9f/p/5.json";

    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Open() {
        HttpRequest httpRequest = HttpRequest.get(DUOCAI_URL);
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        List<OpenresultJsk3> list = new ArrayList<>();
        JSONArray jsks = jsonObject.getJSONArray("JSKS");
        for (int i = 0; i < jsks.size(); i++) {
            JSONObject openObj = jsks.getJSONObject(i);
            OpenresultJsk3 openresultJsk3 = new OpenresultJsk3();
            openresultJsk3.setPlatQs(openObj.getString("issue"));
            openresultJsk3.setOpenResult(openObj.getString("code"));
            openresultJsk3.setOpenStatus(0);
            openresultJsk3.setOpenResultTime(new Date());
            openresultJsk3.setUpdateTime(new Date());
            list.add(openresultJsk3);
        }
        openresultJsk3Service.batchOpenResult(list);
    }
}
