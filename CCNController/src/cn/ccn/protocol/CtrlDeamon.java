package cn.ccn.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ccn.topology.TopyInfo;
import cn.ctrl.conf.CtrlConf;

public class CtrlDeamon implements Runnable{
	private  DatagramSocket socket;
	private  DatagramPacket packet;
	
	public CtrlDeamon() {
		if(socket==null){
			try {
				socket = new DatagramSocket(CtrlConf.deamonPort);
				System.out.println("listen on "+CtrlConf.deamonPort);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}
	
	public  void startDaemon(){
		byte[] buf;
		TopyInfo topy = new TopyInfo();
		ProtocolFunctions protocol = new ProtocolFunctions(topy, socket);
		while(true){
			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				System.out.println("come from:"+packet.getAddress().getHostName()+":"+packet.getPort());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String id = null;
			JSONObject json = null;
			try {
				json = new JSONObject(new String(buf).trim());
				id = json.getString("id");
			} catch (JSONException e) {
				continue;
			}
			
			if(id.equals("0")) {  //������·״̬
				
			}else if(id.equals("1")) {  //��������ע��
				protocol.registerContent(json);
			}else if(id.equals("2")) {  //����·������
				protocol.executePathCompute(json);
			}

		}
	}

	@Override
	public void run() {
		startDaemon();
	}
}
