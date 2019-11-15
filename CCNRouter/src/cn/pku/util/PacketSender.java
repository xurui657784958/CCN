package cn.pku.util;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.ccn.model.protocol.Face;


public class PacketSender {
	static {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public static DatagramSocket socket;
	public static PacketSenderTheadPool pSenderPool = new PacketSenderTheadPool(5, 20, 20, 40);
	
	public static void send(Face f,byte[] b){
		System.out.println("send data to face:"+ f.getFaceID()+ " with ip:"+f.getIp());
		pSenderPool.addTask(f,b);
	}
	
	public static void send(String ip,int port,byte[] b){
		pSenderPool.addTask(ip,port,b);
	}
}

class PacketSenderTheadPool {

	private ThreadPoolExecutor threadPool;	
	
	public PacketSenderTheadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime,int queueSize){
		threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize),new ThreadPoolExecutor.DiscardPolicy());	
	}
	
	public void addTask(Face f,byte[] b){
		threadPool.execute(new ThreadSender(f, b));
	}
	
	public void addTask(String ip,int port,byte[] b){
		threadPool.execute(new ThreadSender(ip, port, b));
	}

}

class ThreadSender implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatagramSocket socket; 
    private DatagramPacket packet = null;
    private byte[] buf;
    private String ip;
    private int port;
    
    public ThreadSender(Face f,byte[] b) {
		// TODO Auto-generated constructor stub
    	ip = f.getIp();
    	port = f.getPort();
    	buf = b;
		socket = PacketSender.socket;
	}
    
    public ThreadSender(String ip,int port,byte[] b) {
		// TODO Auto-generated constructor stub
    	this.ip = ip;
    	this.port = port;
    	buf = b;
    	socket = PacketSender.socket;
		
	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
    	try {			
	        packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);  
	        socket.send(packet);
		} catch (IOException e) {
		} 

	}

}