package cn.linkedcare.springboot.sentinel;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import cn.linkedcare.springboot.redis.config.RedisConfig;


@Configurable
@ComponentScan
@Import(value={RedisConfig.class})
public class Launch {
	
}
