package cn.ccn.model;

import cn.pku.util.NameUtil;

public class ContentObject extends CCNData{
	private String name;
	private byte[] data;
	private int version;
	private int pid;
	private int totalpid;

	public ContentObject() {
		// TODO Auto-generated constructor stub
	}
	
	public ContentObject(String name,int version,int totalpid, int pid,byte[] data){
		this.name = name;
		this.version = version;
		this.totalpid = totalpid;
		this.pid = pid;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCSName(){
		return NameUtil.contentStoreName(name, pid);
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getTotalpid() {
		return totalpid;
	}
	public void setTotalpid(int totalpid) {
		this.totalpid = totalpid;
	}	
	
	public String toString(){
		StringBuffer sbf = new StringBuffer();
		sbf.append(name+"\t").append(totalpid+"\t").append("pid");
		return sbf.toString();
	}
	
}
