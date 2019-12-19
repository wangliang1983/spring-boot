package cn.linkedcare.springboot.sentinel.config;

import java.util.List;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;

import cn.linkedcare.springboot.sentinel.listener.SentinelDegradeRuleListener;
import cn.linkedcare.springboot.sentinel.listener.SentinelFlowRuleListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RuleManagerConfig implements ApplicationListener<ContextRefreshedEvent>{

	
	@SentinelResource
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		List<DegradeRule>  degradeRules = SentinelDegradeRuleListener.getRules();
//		if(!degradeRules.isEmpty()) {
//			log.info("degradeRules:{}",JSON.toJSONString(degradeRules));
//			DegradeRuleManager.loadRules(degradeRules);
//		}
//		
//		List<FlowRule> flowRules = SentinelFlowRuleListener.getRules();
//		if(!flowRules.isEmpty()) {
//			log.info("flowRules:{}",JSON.toJSONString(flowRules));
//			FlowRuleManager.loadRules(flowRules);
//		}
	}

}
