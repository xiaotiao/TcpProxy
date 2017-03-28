package com.zzh.test.tcp;

public class MessageWrapReceivedTask implements Runnable {

	public MessageWrapReceivedTask() {

		System.out.println("ok.................");
	}

	@Override
	public void run() {

		while (true) {
			try {
				MessageWrap message = Constant.messageWrapQueue.take();
				if (message == null) {
					continue;
				}

				MessageUtil.resqHandle(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
