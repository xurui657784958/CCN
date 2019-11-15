package cn.ccn.start;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ccn.ctrl.CtrlProtocol;
import cn.ccn.routing.CheckForFile;
import cn.pku.util.GlobalVar;



public class PIPRouterDeamon {
	public static DatagramSocket socket = null;
	private DatagramPacket recvPacket;
	private DatagramPacket sendPacket;
//	private ExecutorService exec = Executors.newCachedThreadPool();
	
	private byte[] buf;
	public int listenPort;
	
	public PIPRouterDeamon() {
		// TODO Auto-generated constructor stub
		GlobalVar.init();
		this.listenPort = GlobalVar.routerPort;
		try {
			socket = new DatagramSocket(listenPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void startDeamon(){
		System.out.println("Router started on "+listenPort+" !");
		CheckForFile.autoCheckFiles();
		while(true){
			buf = new byte[GlobalVar.packetDataSize+120];
			recvPacket = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(recvPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			exec.execute(new DealPacket(recvPacket, socket));
			new DealPacket(recvPacket, socket).run();
//			Thread t = new Thread(new DealPacket(recvPacket));
//			t.start();
		}
	}
}


