package cn.linkedcare.springboot.c2s.client;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class C2sClientProcessor implements BeanPostProcessor, ApplicationListener<ApplicationEvent> {
	private static final Logger log = LoggerFactory.getLogger(C2sClientProcessor.class);
	private static List<IC2sClient> list = new ArrayList();
	@Value("${zookeeper.url}")
	private String zkUrl;

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof IC2sClient) {
			IC2sClient sc = (IC2sClient) bean;
			list.add(sc);
		}

		return bean;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		log.info("ApplicationEvent:{}", event);
		if (event instanceof ApplicationPreparedEvent) {
			new C2sClientManage(this.zkUrl, list);
		}
	}
}
