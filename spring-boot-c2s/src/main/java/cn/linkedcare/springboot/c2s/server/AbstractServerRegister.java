package cn.linkedcare.springboot.c2s.server;

import com.alibaba.fastjson.JSON;

import cn.linkedcare.springboot.c2s.dto.ServerDto;
import cn.linkedcare.springboot.c2s.utils.IPUtils;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = { "classpath:application-c2s.properties",
		"classpath:application-c2s-${spring.profiles.active}.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8")
public abstract class AbstractServerRegister {
	private static String ip;
	private static String password;
	private static String path;
	private static int port;

	public abstract void init();

	public abstract void destory();

	public static String getConnectServer() {
		return ip + ":" + port;
	}

	public static String getJson() {
		ServerDto serverDto = new ServerDto();
		serverDto.setConnectServer(ip + ":" + port);
		serverDto.setPassword(password);

		return JSON.toJSONString(serverDto);
	}

	@Value("${c2s.zookeeper.path}")
	public void setPath(String path) {
		path = path;
	}

	@Value("${c2s.server.port}")
	public void setPort(int port) {
		port = port;
	}

	public AbstractServerRegister() {
		ip = IPUtils.getIp();

		password = UUID.randomUUID().toString();
	}

	public static String getPath() {
		return path;
	}

	public static String getPassword() {
		return password;
	}
}
