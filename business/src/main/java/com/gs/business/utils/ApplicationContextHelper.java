package com.gs.business.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(Class<T> clazz) {
        //先判断是否为空
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }


    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }

        return applicationContext.getBean(name, clazz);

    }
}
