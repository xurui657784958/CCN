package cn.ccn.topology;

public class NodeInfo{
	public int id;
	public String name;
	public String ip;
	public int port;
	public int previliges = 0;  //1 for ip, 2 for content, 4 for id......7 for all
	
	public NodeInfo(int id,String name,String ip,int port) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public NodeInfo(int id,String name,String ip,int port, int previliges) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.previliges = previliges;
	}
}
