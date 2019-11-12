package cn.linkedcare.springboot.c2s.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.springframework.util.StringUtils;

public class IPUtils {
	private static String ip = System.getenv("easy.im.server.ip");

	public static String getIp() {
		return ip;
	}

	static {
		try {
			if (StringUtils.isEmpty(ip)) {
				Enumeration en = NetworkInterface.getNetworkInterfaces();
				while (en.hasMoreElements()) {
					NetworkInterface i = (NetworkInterface) en.nextElement();
					Enumeration en2 = i.getInetAddresses();
					while (en2.hasMoreElements()) {
						InetAddress addr = (InetAddress) en2.nextElement();
						if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
							ip = addr.getHostAddress();
						}
					}
				}
			}
		} catch (Exception var4) {
			throw new RuntimeException(var4);
		}
		if (ip == null) {
			throw new RuntimeException("ip is null");
		}
	}
}
