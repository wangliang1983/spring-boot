package cn.linkedcare.springboot.sentinel.listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import cn.linkedcare.springboot.sentinel.annotation.DegradeRule;
import cn.linkedcare.springboot.sentinel.annotation.FlowRule;

/**
 * 熔断规则适配
 * @author wl
 *
 */
public class SentinelFlowRuleListener implements BeanPostProcessor{

	private static List<FlowRule> rules = new ArrayList<FlowRule>();
	
	
	public static List<FlowRule> getRules() {
		return rules;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		
		
		for(Method m:methods) {
			FlowRule rule = m.getAnnotation(FlowRule.class);
			
			FlowRule flowFule = new FlowRule();
			
			flowFule.setResource(rule.resourceName());
			flowFule.setGrade(rule.type().getValue());
			flowFule.setCount(rule.count());
			flowFule.setControlBehavior(rule.controlBehavior().getValue());
	
			rules.add(flowFule);
		}
		
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		return bean;
	}

}
