package com.zzh.test.tcp;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageHandler extends SimpleChannelInboundHandler {
	
	private MessageWrap wrap;
	
	public static ChannelHandlerContext ctx;

	public MessageHandler(MessageWrap wrap){
		this.wrap = wrap;
	}
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("MessageHandler [channelActive]..");
//		ctx.writeAndFlush(wrap.getMsg());
		InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
		int code = (localAddress.getHostName() + localAddress.getPort()).hashCode();
		Constant.channelMap.put(code, ctx.channel());
		
	}
	
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		System.out.println("MessageHandler [channelRead]..");
		System.out.println("MessageHandler [channelRead] received msg..."+msg);
		
		final MessageWrap wrap = this.wrap;
		wrap.setMsg(msg);
		//响应消息
		wrap.setType(2);
		//TODO 需要定义代理规则
		wrap.setDestTopic("P1");
		wrap.setRespAddress(wrap.getReqAddress());
		ExecutorsUtil.execTask(new Runnable(){

			public void run() {
				try {
					Constant.messageWrapQueue.put(wrap);
					System.out.println("ctx.channel="+ctx.channel());
//					ctx.channel().read();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("MessageHandler [channelInactive].."); 
		
		InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
		 int code = (localAddress.getHostName() + localAddress.getPort()).hashCode();
		 Constant.channelMap.remove(code);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		 InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
		 int code = (localAddress.getHostName() + localAddress.getPort()).hashCode();
		 Constant.channelMap.remove(code);
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
	}
}
