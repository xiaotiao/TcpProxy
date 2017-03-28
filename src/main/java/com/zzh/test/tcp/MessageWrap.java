package com.zzh.test.tcp;

import java.io.Serializable;
import java.net.InetSocketAddress;

public class MessageWrap implements Serializable{

	private int type;
	private String destTopic;
	private Object msg;
	private InetSocketAddress reqAddress;
	private InetSocketAddress respAddress;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDestTopic() {
		return destTopic;
	}
	public void setDestTopic(String destTopic) {
		this.destTopic = destTopic;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public InetSocketAddress getReqAddress() {
		return reqAddress;
	}
	public void setReqAddress(InetSocketAddress reqAddress) {
		this.reqAddress = reqAddress;
	}
	public InetSocketAddress getRespAddress() {
		return respAddress;
	}
	public void setRespAddress(InetSocketAddress respAddress) {
		this.respAddress = respAddress;
	}
	
	
	@Override
	public String toString() {
		return "MessageWrap [type=" + type + ", destTopic=" + destTopic
				+ ", msg=" + msg + ", reqAddress=" + reqAddress
				+ ", respAddress=" + respAddress + "]";
	}
	
}
