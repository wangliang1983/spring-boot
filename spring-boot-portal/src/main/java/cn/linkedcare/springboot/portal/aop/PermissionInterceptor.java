package cn.linkedcare.springboot.portal.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.linkedcare.mall.common.dto.EnvKeyEnum;
import cn.linkedcare.mall.common.dto.MallResponse;
import cn.linkedcare.mall.common.dto.Result;
import cn.linkedcare.mall.common.dto.auth.SaasContextDto;
import cn.linkedcare.mall.common.dto.util.ExpressionUtil;
import cn.linkedcare.mall.common.dto.util.JwtUtil;
import cn.linkedcare.mall.common.dto.util.Validator;
import cn.linkedcare.springboot.portal.annotation.permission.Permission;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PermissionInterceptor  implements HandlerInterceptor {
	public static final String AUTH = "Authorization";
	public static final String PLATFORM = "platform";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//只有后台做权限验证
		String platform = request.getHeader(PLATFORM);
		if(!"backTenant".equals(platform)){
			return true;
		}
		
		SaasContextDto saasContext = getSaasContextDto(request);
		if(null == saasContext){
			return true;
		}
		
		//口腔和全科暂时没做
		if(EnvKeyEnum.kq.equals(saasContext.getEnvKeyEnum()) || EnvKeyEnum.qk.equals(saasContext.getEnvKeyEnum())){
			return true;
		}

		
		boolean result =  hasPermission(handler, saasContext.getPermissionList());
		if(!result) {
			MallResponse<String> res = new MallResponse<String>();
			res.setResult(Result.authServerError.getCode());
			res.setBody("premission error!");
			response.getWriter().write(JSON.toJSONString(res));
		}
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	private boolean hasPermission(Object handler, List<String> permissionList) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
           
            
            // 如果方法上的注解为空 则获取类的注解
            if (permission == null) {
            	permission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);
            }
            
            // 如果标记了注解，则判断权限
            if (permission != null && Validator.notNullAndEmpty(permission.value()) ) {
            	if(null == permissionList || 0 >= permissionList.size()){
            		return false;
            	}
            	log.info("permission.value:{}, permissionList:{}", permission.value(), JSON.toJSON(permissionList));
            	if(ExpressionUtil.evaluate(permission.value(), new HashSet<>(permissionList))){
            		//有权限
            		return true;
            	}
                
            	return false;
            }else{
            	//如果没有标记注解,则无需进行权限认证
            	return true;
            }
        }
        return true;
    }
	
	public SaasContextDto getSaasContextDto(HttpServletRequest request){
	    String auth = request.getHeader(AUTH);
	    if(auth == null){
	        log.warn("auth header: {} is null", AUTH);
	        return null;
	    }
	    
		String platform = request.getHeader(PLATFORM);
		
		log.info("http headers:{},{}",platform,JSON.toJSONString(request.getHeaderNames()));
		
		
		SaasContextDto saasContextDto = JwtUtil.getSaaSContext(auth);
	    
	    return saasContextDto;
	}
}

