package com.zzh.test.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * 
 * 发送消息的模板方法，定义发送的基本规则
 *
 */
public final class MessageUtil {
	
	private static final Bootstrap b = new Bootstrap();
	private static EventLoopGroup group = new NioEventLoopGroup();
//	private static Channel outboundChannel = null;
	
	static ChannelFuture f;
	static{
		 b.group(group).channel(NioSocketChannel.class)
		 
		 .handler(new LoggingHandler())
	        .option(ChannelOption.SO_REUSEADDR, true)
	        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TcpProxyServer.getConfig().getInt("tcpProxyServer.connectTimeoutMillis"))
	        .option(ChannelOption.SO_KEEPALIVE, true);
		 
		
//			try {
//				f = b.connect("localhost",3306).sync();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}

	/**
	 * 请求方处理方法
	 */
	public static void reqHandle(MessageWrap msg){
		msg.setType(1);
		//TODO 需要定义代理规则
		msg.setDestTopic("P2");
		//TODO 发送给消息队列
		try {
			Constant.messageWrapQueue.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 响应方处理方法
	 */
	public static void resqHandle(final MessageWrap msg){
		//TODO 从指定topic中接收消息
		try {
			//代发请求
			if(msg.getType() == 1){
				//TODO 获取代理请求消息
				InetSocketAddress respAddress = msg.getRespAddress();
				
				int code = (respAddress.getHostName()+respAddress.getPort()).hashCode();
				System.out.println("hostName="+respAddress.getHostName() +" port="+respAddress.getPort());
				System.out.println("hashCode="+code);
				Channel channel = Constant.channelMap.get(code);
//				final ChannelFuture f ;
				
				if(channel == null){
//					Bootstrap b = new Bootstrap();
//					 b.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
////					 .handler(new LoggingHandler())
//				        .option(ChannelOption.SO_REUSEADDR, true)
//				        .option(ChannelOption.SO_KEEPALIVE, true);
//					 b.handler(new MessageHandler(msg));
					
					if(f == null || !f.channel().isActive()){
						System.out.println("bootstrap connection..");
						f = b.connect("localhost",3306).sync();
						f.channel().pipeline().addLast(new MessageHandler(msg));
					}
					final Channel outboundChannel = f.channel();
//					f.channel().pipeline().remove(MessageHandler.class);
					
					
					if(msg.getMsg() instanceof EmptyByteBuf){
						System.out.println("==========================EmptyByteBuf============"+msg.getMsg());
						
					}else{
						outboundChannel.writeAndFlush(msg.getMsg());
					}
					
//					f.addListener(new ChannelFutureListener() {
//						
//						@Override
//						public void operationComplete(ChannelFuture future) throws Exception {
//							if(future.isSuccess()){
//								outboundChannel.writeAndFlush(msg.getMsg());
//							}else{
//								outboundChannel.close();
//							}
//							
//						}
//					});
					System.out.println(msg.getMsg());
					
					System.out.println("outboundChannel = "+outboundChannel);
					
				}
				
			}else if(msg.getType() == 2){
				InetSocketAddress respAddress = msg.getRespAddress();
				//TODO 获取该响应地址的channel
				int code = (respAddress.getHostName()+respAddress.getPort()).hashCode();
				final Channel channel = Constant.channelMap.get(code);
				if(channel != null){
					ByteBuf buf = (ByteBuf) msg.getMsg();
					System.out.println("inboundChannel="+channel);
					
					System.out.println("buf="+buf);
					channel.writeAndFlush(buf).sync();
//					channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
//						
//						@Override
//						public void operationComplete(ChannelFuture future) throws Exception {
//							
//							System.out.println("{operationComplete} future result="+future.isSuccess());
//							if(future.isSuccess()){
////								future.channel().read();
//							}else{
//								
//								System.out.println("inboundChannel close .....");
////								channel.close();
//							}
//						}
//					});
				}
			}else{
				//TODO 未知的消息类型，丢弃
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		
		final MessageWrap wrap = new MessageWrap();
		 b.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(
						new MessageHandler(wrap));
			}
		});
	        
		 
		for(int i=0;i<100;i++){
			final ChannelFuture f = b.connect("localhost",3306).sync();
//			f.channel().closeFuture().sync();
		}
	
//		f.channel().writeAndFlush("http://localhost:8080/netty/user/pingpong.do");
//		f.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) {
//				if (future.isSuccess()) {
//					// connection complete start to read first data
//					f.channel().read();
//				} else {
//					// Close the connection if the connection attempt has failed.
////					f.channel().close();
//				}
//			}
//		});
	}
	
}
