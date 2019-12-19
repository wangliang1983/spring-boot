package cn.linkedcare.springboot.sentinel.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

public @interface SentinelDegradeRule {
	
	public static enum DegradeRuleType{
		averageRT(RuleConstant.DEGRADE_GRADE_RT),//平均响应时间
		exceptionRatio(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO),//异常的比列
		exceptionCount(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);//异常的个数
		;
		
		private int value;

		private DegradeRuleType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	
	
	String resourceName();//资源名称
	
	DegradeRuleType type() default DegradeRuleType.exceptionRatio;//熔断类型
	
	double count() default 0.8;//默认异常比列为80%的时候熔断

	int timeWindow() default 10;//时间窗口内熔断
	
	
}
