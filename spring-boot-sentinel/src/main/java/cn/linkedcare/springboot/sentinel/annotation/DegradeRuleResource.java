package cn.linkedcare.springboot.sentinel.annotation;

/**
 * 熔断相关资源
 * @author wl
 *
 */
public @interface DegradeRuleResource {
	/**
	 * 熔断ku'c
	 * @return
	 */
	DegradeRule degradeRule();
	
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
