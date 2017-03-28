
package com.zzh.test.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetSocketAddress;
import java.util.LinkedList;


public class ProxyFrontendHandler extends SimpleChannelInboundHandler {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(ProxyFrontendHandler.class);

    private Channel inboundChannel;
    private Channel outboundChannel;
    private LinkedList<Object> inboundMsgBuffer = new LinkedList<Object> ();

    enum ConnectionStatus{
        init,
        outBoundChnnlConnecting,      //inbound connected and outbound connecting  
        outBoundChnnlReady,           //inbound connected and outbound connected    
        closing                       //closing inbound and outbound connection
    }

    private ConnectionStatus connectStatus = ConnectionStatus.init;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        
    	System.out.println("ProxyFrontendHandler [channelActive]...");
    	inboundChannel = ctx.channel();
        InetSocketAddress localAddress = (InetSocketAddress) inboundChannel.localAddress();
        int port = localAddress.getPort();
        int code = (localAddress.getHostName()+localAddress.getPort()).hashCode();
        Constant.channelMap.put(code, inboundChannel);
        
//        inboundChannel.read();
//        
        final MessageWrap wrap = emptyMessageWrap();
    	wrap.setMsg(Unpooled.wrappedBuffer(new byte[]{}));
    	wrap.setType(1);
    	
    	MessageUtil.reqHandle(wrap);
    	
    	System.out.println("ProxyFrontendHandler [channelActive]...msg"+wrap.getMsg());
    }

    public Channel getInboundChannel() {
        return inboundChannel;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	System.out.println("ProxyFrontendHandler [channelRead]");
    	final MessageWrap wrap = emptyMessageWrap();
    	wrap.setMsg(msg);
    	wrap.setType(1);
    	
    	MessageUtil.reqHandle(wrap);
    	
    	System.out.println("ProxyFrontendHandler [channelRead]...msg"+wrap.getMsg());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("ProxyFrontendHandler|channelInactive");
        log.debug("ProxyFrontendHandler|channelInactive");
        InetSocketAddress localAddress = (InetSocketAddress) inboundChannel.localAddress();
        int code = (localAddress.getHostName()+localAddress.getPort()).hashCode();
        Constant.channelMap.remove(code);
        close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("ProxyFrontendHandler|exceptionCaught|remoteAddress="+ctx.channel().remoteAddress(), cause);
        close();
    }


    public void closeOnFlush(Channel ch) {
//        if (ch.isActive()) {
//            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//        }
    }

    public void close() {
        connectStatus = ConnectionStatus.closing;
        for(Object obj : inboundMsgBuffer){
            release(obj);
        }
        inboundMsgBuffer.clear();
        closeOnFlush(inboundChannel);
        closeOnFlush(outboundChannel);
    }

    public void outBoundChannelReady() {
        inboundChannel.config().setAutoRead(true);
        connectStatus = ConnectionStatus.outBoundChnnlReady;
        for(Object obj : inboundMsgBuffer){
            outboundChannel.writeAndFlush(obj);
        }
        inboundMsgBuffer.clear();
    }

    //call by 
    //To avoid write too fast to outbouond connection, We need to disable inbound auto read if outbound 's writebuffer is full
    public void setAutoRead(boolean autoRead){
        inboundChannel.config().setAutoRead(autoRead);
    }

    private void release(Object obj){
        if(obj instanceof ByteBuf){
            ((ByteBuf)obj).release();
        }
    }
    
    
    private MessageWrap emptyMessageWrap(){
    	 InetSocketAddress localAddress = (InetSocketAddress) inboundChannel.localAddress();
         int port = localAddress.getPort();
         final TcpProxyServer.ProxyHost outboundRemoteHost = TcpProxyServer.getProxyHosts().get(port);
         
    	
    	final MessageWrap wrap = new MessageWrap();
    	wrap.setReqAddress(localAddress);
    	
    	InetSocketAddress respAddress = new InetSocketAddress(outboundRemoteHost.getRemoteHost(), outboundRemoteHost.getRemotePort());
    	wrap.setRespAddress(respAddress);
    	wrap.setMsg(null);
    	
    	return wrap;
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
    
    
}
