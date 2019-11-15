package cn.ccn.topology;

public class PathNode{
	public String name;//node ID
	public String nodeIP;
	public int nodePort;
	public int protocolPort;
	
	public PathNode(String name, String nodeIP ,int nodePort, int protocolPort) {
		this.name = name;
		this.nodeIP = nodeIP;
		this.nodePort = nodePort;
		this.protocolPort = protocolPort;
		
	}
}
