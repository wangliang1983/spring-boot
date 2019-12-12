package cn.linkedcare.springboot.sentinel.filter;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRuleResource;
import cn.linkedcare.springboot.sentinel.annotation.SentinelFlowRuleResource;

@Aspect  
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy=true)
@Component 
@Order(value=Ordered.LOWEST_PRECEDENCE)
public class SentinelResourceFilter {

	/** 
     * 定义拦截规则
     *  and @annotation(org.springframework.web.bind.annotation.RequestMapping)
     */  
    @Pointcut("execution(* cn.linkedcare..controller..*.*(..)) or cn.linkedcare..service..*.*(..)) or execution(* cn.linkedcare..dao..*.*(..))")  
    public void methodPointcut(){} 
    
    /** 
     * 拦截器具体实现 
     * @param pjp 
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。） 
     * @throws Throwable 
     */  
    @Around("methodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里  
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable{  
    	MethodSignature signature = (MethodSignature) pjp.getSignature();  
        Method method = signature.getMethod(); //获取被拦截的方法  
    	
        SentinelDegradeRuleResource degradeRuleResource = method.getAnnotation(SentinelDegradeRuleResource.class);
        if(degradeRuleResource!=null) {
        	
        }
        
        SentinelFlowRuleResource sentinelFlowRuleResource = method.getAnnotation(SentinelFlowRuleResource.class);
        if(degradeRuleResource!=null) {
        	
        }
        
    	return pjp.proceed();
   }
    
   private void doSentinelDegradeRule() {
	   
   }
    
    
}
