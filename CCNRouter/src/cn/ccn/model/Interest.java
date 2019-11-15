package cn.ccn.model;

import cn.pku.util.GlobalVar;
import cn.pku.util.NameUtil;

public class Interest extends CCNData{
	private String name;
	private int port = GlobalVar.routerPort;
	private int pid = 0;
	private int flag = 0;
	
	public Interest() {
		// TODO Auto-generated constructor stub
	}
	
	public Interest(String name, int port, int pid , int flag){
		this.name= name;
		this.port = port;
		this.pid = pid;
		this.flag = flag;
	}
	
	public String getName() { //ipv6 packet
		return name;
	}
	public String csName() { //cs
		return NameUtil.contentStoreName(name, pid);
	}
	public String pfName() {  //fib&pit
		return NameUtil.distinctName(name, pid, flag);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
