package cn.linkedcare.springboot.sentinel.annotation;


/**
 * 限流资源
 * @author wl
 *
 */
public @interface FlowRuleResource {
	
	/**
	 * 限流规则
	 * @return
	 */
	FlowRule sentinelFlowRule();


	/**
	 * 限流后处理的方法
	 * @return
	 */
    String blockHandlerMethod() default "";

    
    /**
     * 限流后处理的类型
     * @return
     */
    Class<?> blockHandlerClass() default Void.class;
	
}
