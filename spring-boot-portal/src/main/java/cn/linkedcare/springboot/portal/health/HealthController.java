package cn.linkedcare.springboot.portal.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.linkedcare.mall.common.dto.MallResponse;


@Controller
public class HealthController {
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public MallResponse<Object> health(){
			return MallResponse.ok(null);
	}
}
