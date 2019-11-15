package cn.ccn.ctrl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ccn.model.Interest;
import cn.ccn.model.protocol.FibItem;
import cn.ccn.packet.PacketTools;
import cn.ccn.routing.FibManager;
import cn.pku.util.GlobalVar;
import cn.pku.util.PacketSender;

public class CtrlProtocol {
	public static DatagramSocket socket;
	public static int port = GlobalVar.protocolPort;
	public DatagramPacket packet;
	
	static{
		if(socket==null){
			try {
				socket = new DatagramSocket(GlobalVar.ctrlRecvPort);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			new Thread(new ProtocolRecv(socket)).start();
		}
	}
	
	public CtrlProtocol() {
	
	}
	
	
	public void transferNameLSA(JSONObject json){
		try {
			json.put("id", 1);
			json.put("rn", GlobalVar.routerName);			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		send(json.toString());
	}
	
	public void transferContentFib(String content,int pid, int net){
		JSONObject json = new JSONObject();
		try {
			json.put("id", 2);
			json.put("rn", GlobalVar.routerName);
			json.put("cn", content);
			json.put("net", net);
			json.put("pid", pid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		send(json.toString());
	}
	
	
	/* send tool */
	public void send(String str){
		System.out.println("***ToCtrl*** "+str);
		byte[] buf = str.getBytes();
		try {
			packet = new DatagramPacket(buf, buf.length,InetAddress.getByName(GlobalVar.protocolIP),port);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*receive protocol from ctrl*/
class ProtocolRecv implements Runnable{
	private DatagramSocket socket;
	DatagramPacket packet;
	byte[] buf;
	
	public ProtocolRecv(DatagramSocket socket) {
		this.socket = socket;
	}


	@Override
	public void run() {
		System.out.println("Router protocol started on " + socket.getLocalPort()+" !");
		while(true){
			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject json;
			try {
				json = new JSONObject(new String(packet.getData()).trim());
				///////////////protocol//////////
				if(json.getInt("id")==1)handleNeighboor(json);
				if(json.getInt("id")==2)handleFIB(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}//end of run
	
	public void handleNeighboor(JSONObject json){
		
	}
	
	public void handleFIB(JSONObject json){
		String from=null,to=null,content=null;boolean flag = false;
		try {
			from = json.getString("from");
			to = json.getString("to");
			content = json.getString("content");
			flag = json.getBoolean("flag");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FibItem fi = new FibItem(content, GlobalVar.neighbours.get(to).getFace().getFaceID());
		FibManager.insertFib(fi);
		if(flag){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String temp[] = content.split("/SN=");
			Interest in = new Interest();
			in.setName(temp[0]);
			in.setPort(GlobalVar.routerPort);
			temp = temp[1].split("/F=");
			in.setPid(Integer.parseInt(temp[0]));
			in.setFlag(Integer.parseInt(temp[1]));
			PacketSender.send(GlobalVar.neighbours.get(to).getFace(), PacketTools.InterestToByte(in));
		}
	}
}