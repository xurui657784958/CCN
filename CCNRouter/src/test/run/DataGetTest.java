package test.run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cn.ccn.model.ContentObject;
import cn.ccn.model.Interest;
import cn.ccn.packet.PacketTools;
import cn.pku.util.GlobalVar;

public class DataGetTest {
	private static DatagramSocket socket = null;
	public static void main(String[] args) {
		String filename = "router0_001.pdf";
		File file = new File("CCNRouter\\download"+File.separator+filename);
		if(file.exists())file.delete();
		downloadFile(filename);
	}
	
	public static void downloadFile(String name){
		try {
			if(socket==null)socket = new DatagramSocket();
			Interest in = new Interest(name ,socket.getLocalPort(), 1, 2);
			byte[] buf = PacketTools.InterestToByte(in);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(GlobalVar.routerIp), GlobalVar.routerPort);
			socket.send(packet);
			buf = new byte[GlobalVar.packetDataSize+120];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			ContentObject co = (ContentObject)PacketTools.parse(packet.getData());
			writeToFile(co.getName(), co.getData());
			for(int i=2;i<=co.getTotalpid();i++){
				in.setPid(i);
				buf = PacketTools.InterestToByte(in);
				packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(GlobalVar.routerIp), GlobalVar.routerPort);
				socket.send(packet);
				buf = new byte[GlobalVar.packetDataSize+120];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				co = (ContentObject)PacketTools.parse(packet.getData());
				writeToFile(co.getName(), co.getData());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(String name,byte[] data){
		File file = new File("CCNRouter\\download"+File.separator+name);
		try {
			if(!file.exists())file.createNewFile();
			OutputStream out = new FileOutputStream(file,true);
			out.write(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
