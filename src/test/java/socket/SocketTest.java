package socket;

//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.Test;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//
//import com.zzh.test.tcp.ExecutorsUtil;

//import static org.junit.Assert.*;
//
//public class SocketTest {
//	public static String content ;
//	
//	static{
//		 Resource res = new FileSystemResource(new File("E:\\company\\wondersoft\\工作\\工作\\工作簿\\04\\27\\1.txt"));
//		 try(
//				 InputStream is = res.getInputStream();
//				 ) {
//			
//			 byte[] buf = new byte[is.available()];
//			 is.read(buf);
//			 content = new String(buf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) throws IOException {
//		ServerSocket serverSocket = new ServerSocket(9999);
//		
//		while(true){
//		
//			final Socket socket = serverSocket.accept();
//			ExecutorsUtil.execTask(new Runnable(){
//
//				@Override
//				public void run() {
//					try(InputStream is = socket.getInputStream();
//						PrintStream ps = new PrintStream(socket.getOutputStream())){
//						
//						byte[] buf = new byte[is.available()];
//						
//						is.read(buf);
//						System.out.println(new String(buf));
//						
//						StringBuilder sb = new StringBuilder();
//						for(int i=0;i<2000;i++){
//							sb.append("ok");
//						}
//						ps.print(sb.toString()+System.getProperty("line.separator"));
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}
//				
//			});
//		}
//	}
//	
//	
//	@Test
//	public void testScoket(){
//		
//		try {
//			
//			while(true){
//				Socket socket = new Socket("localhost", 50088);
//				OutputStream os = socket.getOutputStream();
//				os.write(content.getBytes());
//				os.flush();
//				InputStream is = socket.getInputStream();
//				while(is.available() == 0){
//					
//					
//				}
//				
//				byte[] buf = new byte[is.available()];
//				is.read(buf);
////				assertEquals("pingpong", new String(buf));
//				
//				TimeUnit.SECONDS.sleep(1);
//				System.out.println(new String(buf));
//				
//				os.write(content.getBytes());
//				os.flush();
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void testMysqlSocket(){
//		try {
//			Socket socket = new Socket("localhost",3306);
//			InputStream is = socket.getInputStream();
//			while(is.available() == 0 ){
//				
//				
//			}
//			
//			byte[] buf = new byte[is.available()];
//			is.read(buf);
//			System.out.println(new String(buf));
//			
//			int count = 0;
//			boolean flag = socket.isConnected();
//			while(flag){
//				
//				TimeUnit.SECONDS.sleep(1);
//				System.out.println("connection..seconds="+(++count));
//				flag = socket.isConnected();
//			}
//			
//			System.out.println("disconnection...");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
