package cn.linkedcare.springboot.sentinel.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

/**
 * 限流规则
 * @author wl
 *
 */
public @interface SentinelFlowRule {
	public static enum SentinelFlowType{
		qps(RuleConstant.FLOW_GRADE_QPS),
		thread(RuleConstant.FLOW_GRADE_THREAD);
		
		private int value;
		
		SentinelFlowType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	
	
	public static enum ControlBehavior{
		defaults(RuleConstant.CONTROL_BEHAVIOR_DEFAULT),
		warmUp(RuleConstant.CONTROL_BEHAVIOR_WARM_UP),
		rateLimiter(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER),
		warmUpAndrateLimiter(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
		
		private int value;
		
		ControlBehavior(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	
	String resourceName();//资源名称
	
	SentinelFlowType type() default SentinelFlowType.thread;//限制最大访问线程

	double count();//相关个数
	
	ControlBehavior controlBehavior() default ControlBehavior.defaults; 
	
}
