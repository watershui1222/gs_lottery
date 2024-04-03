package com.gs.commons.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BeanFactoryUtil {
    @Resource
    private ApplicationContext context;

    public Object getBean( String beanName ) {
        return context.getBean( beanName );
    }

    public ApplicationContext getInstance() {
        return this.context;
    }
}
