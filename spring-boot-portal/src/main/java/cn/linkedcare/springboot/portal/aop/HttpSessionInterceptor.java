package cn.linkedcare.springboot.portal.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWTVerifier;

import cn.linkedcare.mall.common.dto.auth.MallContextDto;
import cn.linkedcare.mall.common.dto.auth.SaasContextDto;
import cn.linkedcare.mall.common.dto.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpSessionInterceptor  implements HandlerInterceptor{
	
	
	//前台session
	private static ThreadLocal<MallContextDto> frontThreadLocal = new ThreadLocal<MallContextDto>();
	
	//后台session
	private static ThreadLocal<SaasContextDto> backThreadLocal = new ThreadLocal<SaasContextDto>();
	
	
	public static final String PLATFORM = "platform";
	public static final String AUTH     = "Authorization";

	/**
	 * 得到前台session
	 * @return
	 */
	public static MallContextDto getFrontSession() {
		log.info("getFrontSession:{}",JSON.toJSONString(frontThreadLocal.get()));
		
		return frontThreadLocal.get();
	}

	/**
	 * 得到后台session
	 * @return
	 */
	public static SaasContextDto getBackSession() {
		log.info("getBackSession:{}",JSON.toJSONString(backThreadLocal.get()));
		
		return backThreadLocal.get();
	}
	
	

	/**
	 * 是否是jwt token
	 * @param request
	 * @return
	 */
	private boolean isJWTToken(HttpServletRequest request){
		String uri = request.getRequestURI();
		//此方法不做拦截
		if(uri.contains("getJWTCode")) {
			return false;
		}
		
		String token = request.getHeader(AUTH);
		if(token==null){
			return false;
		}
		final String[] pieces = token.split("\\.");
        if (pieces.length != 3) {
        	return false;
        }
        	return true;
	}
	
	/**
	 * 设置前端的session
	 * @param request
	 * @return
	 */
	private void setFrontSession(HttpServletRequest request){
		if(!isJWTToken(request)){
			return;
		}
		
		String token = request.getHeader(AUTH);
		log.info("front content:{}",JSON.toJSONString(token));
		
		MallContextDto mallContextDto = JwtUtil.getWxContext(token);
		frontThreadLocal.set(mallContextDto);
	}
	
	/**
	 * 设置后台session
	 * @param request
	 */
	private void setBackSession(HttpServletRequest request){
		if(!isJWTToken(request)){
			return;
		}
		
		String token = request.getHeader(AUTH);
		log.info("back content:{}",JSON.toJSONString(token));
		
		SaasContextDto saasContextDto = JwtUtil.getSaaSContext(token);
		backThreadLocal.set(saasContextDto);
	}
	
	public static void main(String[] args) {
		String str ="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhcHAiOm51bGwsIm9yZyI6ImMzMjFlODA3NmVjNzQyYzdiMTFmMzU5MjAxNGRkNzc0IiwiY2FkIjp0cnVlLCJkcHQiOm51bGwsIm1pZCI6MCwidHlwIjpudWxsLCJ0aWQiOiIwMDAxIiwidWlkIjoiZTk3YTRkYmM5YTZjNDM0Mzk1MWI0MGY3YmM5OTJkZDEiLCJiaXoiOiJ5bSIsInBtcyI6WyJub3RpZmljYXRpb24iLCJub3RpY2VDb25maWciLCJub3RpY2VMb2ciLCJtYWxsIiwibWFsbENvbmZpZyIsIm1hbGxDbXMiLCJtYWxsQ2xpbmljIiwiY29udGFjdE1rIiwiY29udGFjdE1rLWVkaXQiLCJjb250YWN0TWstZXhwb3J0Iiwic2V0Q2VudGVyIiwiaG9tZURlY29yYXRpb24iLCJjdXN0b21lclNvdXJjZSIsImNsaW5pY1NldCIsInNldHVwIiwicGF5Iiwic2V0VWkiLCJzZXJ2aWNlIiwic2VydmljZS1lZGl0IiwiY3VzdG9tZXJTdGF0dXMiLCJjdXN0b21lck1vZGUiLCJ0YWxrTW9kZSIsImRzTW9kZVNldCIsImNzTW9kZVNldCIsInNjcm0uY2hhdC5oaXN0b3J5LnZpZXciLCJzY3JtLndlaXhpbi5zZXJ2aWNlIiwibWtzb2x1dGlvbiIsInNvbHV0aW9uTGlzdCIsIm1rc29sdXRpb25NYW5hZ2UiLCJta3NvbHV0aW9uTWFuYWdlLWV4cG9ydCIsInd4UHVibGljIiwiY3VzdG9tTWVudSIsImZvbGxvd1JlcGx5Iiwia2V5d29yZFJlcGx5IiwiZmFuc01hbmFnZSIsImZhbnNNYW5hZ2UtZXhwb3J0IiwiYWRtaW4iLCJhZG1pbi1lZGl0IiwiZGVjb3JhdGlvbiIsIm1hbGxBY3Rpdml0eSIsInByb21vdGlvbiIsInByb21vdGlvbi1leHBvcnQiLCJwaW5MaXN0IiwicGluTGlzdC1lZGl0IiwiYm9udXNNYWxsIiwiYm9udXNNYWxsLWVkaXQiLCJzZWNraWxsIiwic2Vja2lsbC1lZGl0IiwibWlucHJvIiwibWlucHJvLWVkaXQiLCJncmFwaGljTWFya2V0aW5nIiwic2VuZEFjdGlvbiIsInNlbmRMb2ciLCJpbWFnZVRleHRMaXN0IiwiaW1nTGlzdCIsInNjcm0ud2VpeGluLmNoYXQiLCJpbVNldC1jb3Vwb24iLCJta2NvdXBvbiIsImNvdXBvbkxpc3QiLCJjb3Vwb25NYW5hZ2UiLCJjb3Vwb24tZXhwb3J0Iiwib3JkZXIiLCJvcmRlckxpc3QiLCJvcmRlckxpc3Qtc2VuZCIsIm9yZGVyTGlzdC1jYW5jZWwiLCJvcmRlckxpc3QtZXhwb3J0Iiwib3JkZXJTYWxlIiwib3JkZXJTYWxlLWVkaXQiLCJvcmRlckNvbmZpZyIsIm9yZGVyV3JpdGVPZmYiLCJjbGluaWNTdmMiLCJjbGluaWNRcmNvZGUiLCJjdXN0b21lck1zZyIsInFyY29kZUxpc3QiLCJxcmNvZGVMaXN0LWVkaXQiLCJxcmNvZGVMaXN0LWV4cG9ydCIsIm9mZkxpbmUiLCJvZmZMaW5lLWVkaXQiLCJvZmZMaW5lLWV4cG9ydCIsImludml0ZUZyaWVuZHMiLCJpbnZpdGVSdWxlIiwiaW52aXRlQWxsIiwiaW52aXRlU3RhdGlzdGljcyIsImludml0ZVN0YXRpc3RpY3MtZXhwb3J0Iiwic3RhZmZRcmNvZGUiLCJnb29kcyIsImdvb2RzTGlzdCIsImdvb2RzQ2F0YWxvZyIsImdvb2RzR3JvdXAiLCJhZHZpY2UiLCJhZHZpY2VBZnRlciIsImF1dG9BcHBvaW50IiwiYXBwb2ludFNldCIsImRvY3RvclNldHRpbmciLCJwcm9qZWN0U2V0dGluZyIsImRpc2Vhc2VEZXNjIiwic3ZjRXZhbCIsImV2YWxTZXQiLCJldmFsTGlzdCIsImV2YWxTdW1tYXJ5IiwiZXZhbC1leHBvcnQiXSwiY3luIjoi6aKG5YGl5oC76YOoIiwibmFtIjpudWxsLCJqb2IiOm51bGwsImx0ZCI6ImQ2ODVmZjMwNmNhNTRiMWVhMDZiNzA3ZmIwZjU3NjU5In0.AggjQLhO_alw3gswjO2_5RG7c0qd4AUEWHOeycLBaKFz3KiYbmCp8F9RBNKb8X8FT0LoT9wFbL-LRvy2EJ0M5g";
	
		JwtUtil.getSaaSContext(str);
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String platform = request.getHeader(PLATFORM);
		
		log.info("http headers:{},{}",platform,JSON.toJSONString(request.getHeaderNames()));
		
		
		if(platform==null){
			return true;
		}
		
		switch(platform){
		case "backTenant":
			setBackSession(request);
			break;
		case "frontH5":
			setFrontSession(request);
			break;
		case "backLinkedcare":
			
			break;
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		
		String platform = request.getHeader(PLATFORM);
		if(platform==null){
			return;
		}
		
		
		switch(platform){
		case "backTenant":
			backThreadLocal.remove();
			break;
		case "frontH5":
			frontThreadLocal.remove();
			break;
		case "backLinkedcare":
			
			break;
		}
	}

}

