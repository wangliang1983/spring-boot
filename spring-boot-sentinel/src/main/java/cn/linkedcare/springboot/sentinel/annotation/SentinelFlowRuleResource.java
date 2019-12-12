package cn.linkedcare.springboot.sentinel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流资源
 * @author wl
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SentinelFlowRuleResource {
	
	/**
	 * 限流规则
	 * @return
	 */
	SentinelFlowRule sentinelFlowRule();


	/**
	 * 限流后处理的方法
	 * @return
	 */
    String blockHandlerMethod();

    

	
   
}
