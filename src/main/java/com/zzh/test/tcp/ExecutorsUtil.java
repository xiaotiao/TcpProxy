package com.zzh.test.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Package:com.wondersoft.dt.base.util
 * @Description:
 * @author:quwu
 * @Version：v1.0
 * @ChangeHistoryList：version author date description v1.0 quwu 2014-8-25
 *                            下午20:44:04
 */

public class ExecutorsUtil {
	private static int THREAD_COUNT=30;
	private static ExecutorService exec;
	private static Object lock=new Object();
	public static ExecutorService getExec(){
		synchronized(lock){
			if(exec==null){
				exec = Executors.newFixedThreadPool(THREAD_COUNT);
			}
		}
		return exec;
	}
	
	public static void execTask(Runnable task){
		ExecutorService executor = getExec();
		executor.execute(task);
	}
	
	public static void execTasks(Runnable task, int size){		
		for(int i = 0 ; i < size;i++){
			execTask(task);
		}
	}
}
