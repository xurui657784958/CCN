package test.run;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import cn.ccn.model.ContentObject;
import cn.ccn.model.Interest;
import cn.ccn.packet.PacketTools;
import cn.ccn.start.PIPRouterDeamon;
import cn.pku.util.GlobalVar;

public class RouterTest {
	/*static DatagramSocket socket = null;
	static{
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}*/
	public static void main(String[] args) {
//		new Thread(new TestTask()).start();
		PIPRouterDeamon deamon = new PIPRouterDeamon();
		deamon.startDeamon();
	}
	
	/*public static void sendInterest(String name, int pid, int flag){
		try {
			Interest in = new Interest(name, socket.getLocalPort(), pid, flag);
			byte[] buf = PacketTools.InterestToByte(in);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(GlobalVar.routerIp), GlobalVar.routerPort);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendContent(String name, int version, int totalpid, int pid, byte[] data){
		try {	
			ContentObject co = new ContentObject(name, version, totalpid, pid, data);
			byte[] buf = PacketTools.ContentToByte(co);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(GlobalVar.routerIp), GlobalVar.routerPort);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAndReturn(String name, int pid, int flag){
		try {
			Interest in = new Interest(name, socket.getLocalPort(), pid, flag);
			byte[] buf = PacketTools.InterestToByte(in);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(GlobalVar.routerIp), GlobalVar.routerPort);
			socket.send(packet);
			buf = new byte[1024];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			ContentObject co = (ContentObject)PacketTools.parse(packet.getData());
			System.out.println(co.getName());
			System.out.println(new String(co.getData()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class TestTask implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RouterTest.sendInterest("router0_001.pdf", 5, 2);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RouterTest.sendContent("/hello", 3, 15, 5, "helloworld".getBytes());
		RouterTest.sendAndReturn("/hello", 5, 2);
	}
	*/
}
