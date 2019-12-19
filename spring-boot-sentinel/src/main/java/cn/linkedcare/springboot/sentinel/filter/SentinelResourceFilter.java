package cn.linkedcare.springboot.sentinel.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.util.MethodUtil;

import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRule;
import cn.linkedcare.springboot.sentinel.annotation.SentinelDegradeRuleResource;
import cn.linkedcare.springboot.sentinel.annotation.SentinelFlowRule;
import cn.linkedcare.springboot.sentinel.annotation.SentinelFlowRuleResource;

@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class SentinelResourceFilter {

	@Resource
	private BeanFactory beanFactory;

	/**
	 * 定义拦截规则
	 * and @annotation(org.springframework.web.bind.annotation.RequestMapping)
	 */
	@Pointcut("execution(* cn.linkedcare..controller..*.*(..)) or cn.linkedcare..service..*.*(..)) or execution(* cn.linkedcare..dao..*.*(..))")
	public void sentinelResourceFilter() {
	}

	/**
	 * 拦截器具体实现
	 * 
	 * @param pjp
	 * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
	 * @throws Throwable
	 */
	@Around("sentinelResourceFilter()") // 指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
	public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法

		SentinelDegradeRuleResource degradeRuleResource = method.getAnnotation(SentinelDegradeRuleResource.class);
		if (degradeRuleResource != null) {

			return doSentinelDegradeRule(degradeRuleResource, pjp);
		}

		SentinelFlowRuleResource flowRuleResource = method.getAnnotation(SentinelFlowRuleResource.class);
		if (flowRuleResource != null) {
			return doSentinelFlowRule(flowRuleResource, pjp);
		}

		return pjp.proceed();
	}

	private Object invokeBlockMethod(String blockHandlerMethod, ProceedingJoinPoint pjp) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object object = pjp.getTarget();
		Class<?> classzz = AopUtils.getTargetClass(pjp.getTarget());

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法

		Method blockMethod = classzz.getMethod(blockHandlerMethod, method.getParameterTypes());

		return blockMethod.invoke(object, pjp.getArgs());
	}

	static {
		List<DegradeRule> rules = new ArrayList<DegradeRule>();
		DegradeRule rule = new DegradeRule();
		rule.setResource("GoodsSearchController.selectSuggest");
		// set limit exception ratio to 0.1
		rule.setCount(0.1);
		rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
		rule.setTimeWindow(10);

		rules.add(rule);
		DegradeRuleManager.loadRules(rules);
	}
	
	private Object doSentinelDegradeRule(SentinelDegradeRuleResource degradeRuleResource, ProceedingJoinPoint pjp)
			throws Throwable {

		Entry entry = null;
		try {
			entry = SphU.entry("GoodsSearchController.selectSuggest");

			// Write your biz code here.
			// <<BIZ CODE>>
			System.out.print(9 / 0);
		} catch (BlockException t) {
			System.out.println("aaaannnccc");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Tracer.trace(e);
			throw new RuntimeException(e);
		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
		return null;
	}

	private Object doSentinelFlowRule(SentinelFlowRuleResource flowRuleResource, ProceedingJoinPoint pjp)
			throws Throwable {

		SentinelFlowRule rule = flowRuleResource.sentinelFlowRule();

		Entry entry = null;
		// 务必保证finally会被执行
		try {
			// 资源名可使用任意有业务语义的字符串
			entry = SphU.entry(rule.resourceName());
			// 被保护的业务逻辑
			return pjp.proceed();
		} catch (BlockException e) {
			return invokeBlockMethod(flowRuleResource.blockHandlerMethod(), pjp);
		} catch (Exception e) {
			e.printStackTrace();
			Tracer.trace(e);

			throw new RuntimeException(e);
		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		List<DegradeRule> rules = new ArrayList<DegradeRule>();
		DegradeRule rule = new DegradeRule();
		rule.setResource("GoodsSearchController.selectSuggest");
		// set limit exception ratio to 0.1
		rule.setCount(0.8);
		rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
		rule.setTimeWindow(10);

		rules.add(rule);
		DegradeRuleManager.loadRules(rules);

		while (true) {

			Thread.sleep(20l);

			Entry entry = null;
			try {
				entry = SphU.entry("GoodsSearchController.selectSuggest");

				// Write your biz code here.
				// <<BIZ CODE>>
				System.out.print(9 / 0);
			} catch (BlockException t) {
				System.out.println("aaaannnccc");
			} catch (Exception e) {
				e.printStackTrace();
				Tracer.trace(e);

			} finally {
				if (entry != null) {
					entry.exit();
				}
			}

		}

	}

}
