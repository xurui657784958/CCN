package cn.ccn.model.protocol;

public class Neighbour {
	private String name;
	private String ip;
	private long delay;
	private Face face;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	public long getDelay() {
		return delay;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public Face getFace() {
		return face;
	}
	public void setFace(Face face) {
		this.face = face;
	}
	public Neighbour(String n,String p,Face f){
		name = n;
		ip = p;
		face = f;
	}
}
