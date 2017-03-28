package com.zzh.test.tcp;

import io.netty.channel.Channel;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class Constant {

	public static ApplicationContext applicationContext;
	
	public static Map<Integer,Channel> channelMap = new ConcurrentHashMap<>();
	
	public static BlockingQueue<MessageWrap> messageWrapQueue = new LinkedBlockingQueue<>();

	public static  String proxyName;

	static {
		Properties prop = new Properties();
		Resource res = new ClassPathResource("param.properties");
		try (InputStream is = res.getInputStream()) {
			prop.load(is);
			
			proxyName = prop.getProperty("proxy_name");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
