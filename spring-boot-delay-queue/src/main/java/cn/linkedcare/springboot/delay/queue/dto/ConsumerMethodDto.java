package cn.linkedcare.springboot.delay.queue.dto;

import java.lang.reflect.Method;

import lombok.Data;

@Data
public class ConsumerMethodDto {
	private Object object;
	private Method method;
	private boolean autoCommit;
	private String[] topics;
}
