package cn.ccn.model.protocol;


import java.util.HashSet;
import java.util.Set;

public class Face {

	private String ip;
	private int port;
	private int faceID;
	private Set<String> pitList;
	
	public Face(String ip,int port,int faceid){
		this.ip = ip;
		this.port = port;
		this.faceID = faceid;
		pitList = new HashSet<String>();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getFaceID() {
		return faceID;
	}

	public void setFaceID(int faceID) {
		this.faceID = faceID;
	}

	public Set<String> getPitList() {
		return pitList;
	}

	public void addPitList(String pit) {
		this.pitList.add(pit);
	}
	
	public void delPitList(String pit) {	
		pitList.remove(pit);
	}
	
	
	
}
