package cn.linkedcare.springboot.sentinel.listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRule;
import cn.linkedcare.springboot.sentinel.annotation.SentinelFlowRule;
import cn.linkedcare.springboot.sentinel.annotation.SentinelFlowRuleResource;

/**
 * 熔断规则适配
 * @author wl
 *
 */
@Component
public class SentinelFlowRuleListener implements BeanPostProcessor{

	private static List<FlowRule> rules = new ArrayList<FlowRule>();
	
	
	public static List<FlowRule> getRules() {
		return rules;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		
		
		for(Method m:methods) {
			SentinelFlowRuleResource flowRuleResource = m.getAnnotation(SentinelFlowRuleResource.class);
			
			if(flowRuleResource!=null) {
				SentinelFlowRule  rule = flowRuleResource.sentinelFlowRule();
				
				FlowRule flowFule = new FlowRule();
				
				flowFule.setResource(rule.resourceName());
				flowFule.setGrade(rule.type().getValue());
				flowFule.setCount(rule.count());
				flowFule.setControlBehavior(rule.controlBehavior().getValue());
		
				rules.add(flowFule);
		
			}
		}
		
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		return bean;
	}

}
