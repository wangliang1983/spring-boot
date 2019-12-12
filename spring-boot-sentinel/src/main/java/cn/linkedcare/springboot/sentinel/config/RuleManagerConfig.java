package cn.linkedcare.springboot.sentinel.config;

import java.util.List;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import cn.linkedcare.springboot.sentinel.listener.SentinelDegradeRuleListener;
import cn.linkedcare.springboot.sentinel.listener.SentinelFlowRuleListener;

@Component
public class RuleManagerConfig implements ApplicationListener<ContextStartedEvent>{

	public void onApplicationEvent(ContextStartedEvent event) {
		List<DegradeRule>  degradeRules = SentinelDegradeRuleListener.getRules();
		DegradeRuleManager.loadRules(degradeRules);
		
		List<FlowRule> flowRules = SentinelFlowRuleListener.getRules();
		FlowRuleManager.loadRules(flowRules);
	}

}
