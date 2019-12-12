package cn.linkedcare.springboot.sentinel.listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;

import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRule;
import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRuleResource;


/**
 * 熔断规则相关
 * @author wl
 *
 */
public class SentinelDegradeRuleListener implements BeanPostProcessor{

	private static List<DegradeRule> rules = new ArrayList<DegradeRule>();
	
	
	public static List<DegradeRule> getRules() {
		return rules;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		
		
		for(Method m:methods) {
			SentinelDegradeRuleResource degradeRuleResource = m.getAnnotation(SentinelDegradeRuleResource.class);
			SentinelDegradeRule rule = degradeRuleResource.degradeRule();
			
			DegradeRule degradeRule = new DegradeRule();
			
			degradeRule.setResource(rule.resourceName());
			degradeRule.setGrade(rule.type().getValue());
			degradeRule.setCount(rule.count());
			degradeRule.setTimeWindow(rule.timeWindow());
			
			rules.add(degradeRule);
		}
		
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
