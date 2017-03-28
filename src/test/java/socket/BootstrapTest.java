package socket;

//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.LineBasedFrameDecoder;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.util.CharsetUtil;
//
//import java.io.File;
//import java.io.InputStream;
//
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;


//public class BootstrapTest {
//
//	public static String content;
//	private static final Bootstrap b = new Bootstrap();
//	private static EventLoopGroup group = new NioEventLoopGroup();
//
//	static {
//		b.group(group)
//				.channel(NioSocketChannel.class)
//				.handler(new ChannelInitializer<Channel>() {
//
//					@Override
//					protected void initChannel(Channel ch) throws Exception {
//						ch.pipeline()
//								.addLast(new StringEncoder(CharsetUtil.UTF_8),
////										new LineBasedFrameDecoder(8192),
//										new StringDecoder(CharsetUtil.UTF_8),
////										new ChunkedWriteHandler(),
//										new DiscardHandler());
//
//					}
//				})
//				.option(ChannelOption.SO_REUSEADDR, true)
//				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
//						TcpProxyServer.getConfig().getInt(
//								"tcpProxyServer.connectTimeoutMillis"))
//				.option(ChannelOption.SO_KEEPALIVE, true);
//
//		Resource res = new FileSystemResource(new File(
//				"E:\\company\\wondersoft\\工作\\工作\\工作簿\\04\\27\\1.txt"));
//		try (InputStream is = res.getInputStream();) {
//
//			byte[] buf = new byte[is.available()];
//			is.read(buf);
//			content = new String(buf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) throws InterruptedException {
//		ChannelFuture f = b.connect("localhost", 50088).sync();
//		f.channel().closeFuture().sync();
//	}
//}
//
//class DiscardHandler extends ChannelInboundHandlerAdapter {
//
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("DiscardHandler [channelActive]");
//		ctx.writeAndFlush(BootstrapTest.content);
//	}
//
//	@Override
//	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//
//		System.out.println("inavtive...");
//	}
//
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg)
//			throws Exception {
//
//		System.out.println("DiscardHandler [channelRead]");
//		System.out.println(msg);
//	}
//}
