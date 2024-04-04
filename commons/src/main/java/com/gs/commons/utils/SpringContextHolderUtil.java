package com.gs.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring上下文
 * @author wangjie
 *
 */
@Component
public class SpringContextHolderUtil implements ApplicationContextAware{

	private static final Logger log = LoggerFactory.getLogger(SpringContextHolderUtil.class);
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolderUtil.applicationContext = applicationContext;
	}

	/**
	 * beanName获取实例
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		if (applicationContext == null) {
			log.error("Spring容器未初始化,获取实例[{}]失败", beanName);
			return null;
		}
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> cla) {
		return applicationContext.getBean(beanName, cla);
	}
}
