package com.gs.business.utils;

import cn.hutool.core.util.ClassUtil;
import com.gs.business.service.PlatService;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ReflectionUtil {

    public static Class getPlatImplByPlatCode(String platCode){
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper("com.gs.business.service.impl.plat" , PlatService.class);
        for (Class<?> clazz : classes) {
            try {
                Object fieldValue = clazz.getDeclaredField("platCode").get(null);
                if (fieldValue.equals(platCode)) {
                    return clazz;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("根据平台代码获取平台实现类发生错误",e);
            }
        }
        log.error("获取平台实现类失败:{}",platCode);
        return null;
    }
}
