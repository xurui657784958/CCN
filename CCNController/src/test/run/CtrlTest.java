package test.run;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ccn.protocol.CtrlDeamon;
import cn.ctrl.conf.CtrlConf;

public class CtrlTest {
	
	/*main  /router/sz/n6 */
	public static void main(String[] args) throws InterruptedException {
		new Thread(new CtrlDeamon()).start();
//		
//		Thread.sleep(1000);
//		
		test("1","/router/sz/n0","/hello",0);
		test("2","/router/sz/n0","/hello",0);
	}
	
	
	/*class define*/
	public static DatagramSocket socket;
	static{
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public static void test(String id,String rn, String cn, int net){
		try {
			JSONObject json = new JSONObject();
			json.put("id", id);
			json.put("rn", rn);
			json.put("cn", cn);
			json.put("net", net);
			byte[] buf = json.toString().getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length,InetAddress.getByName("localhost"),CtrlConf.deamonPort);
			socket.send(packet);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}
}
