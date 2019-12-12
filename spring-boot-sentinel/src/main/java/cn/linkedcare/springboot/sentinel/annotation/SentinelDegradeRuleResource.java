package cn.linkedcare.springboot.sentinel.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 熔断相关资源
 * @author wl
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SentinelDegradeRuleResource {
	/**
	 * 熔断ku'c
	 * @return
	 */
	SentinelDegradeRule degradeRule();
	
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
