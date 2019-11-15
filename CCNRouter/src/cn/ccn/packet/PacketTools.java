package cn.ccn.packet;

import java.awt.image.PackedColorModel;

import cn.ccn.model.CCNData;
import cn.ccn.model.ContentObject;
import cn.ccn.model.Interest;
import cn.pku.util.ByteArrayUtil;

public class PacketTools {
	public static byte[] InterestToByte(Interest in){
		byte[] data;
		data = creatInterestPacket(in.getName(), in.getPid(), in.getPort(),in.getFlag());
		return data;
	}
	
	public static byte[] ContentToByte(ContentObject co){
		byte[] data;
		data = creatDataPacket(co.getName(), co.getVersion(), co.getTotalpid(), co.getPid(),co.getData());
		return data;
	}
	
	
	public static CCNData parse(byte[] packet){
		IPv6Packet iv6 = new IPv6Packet(packet);
		if(iv6.type==(byte)0xc1){
			return parseToInterest(iv6);
		}else if(iv6.type==(byte)0xc2)return parseToContentObject(iv6);
		else 
			return null;
	}
	
	public static Interest parseToInterest(IPv6Packet iv6){
		Interest in = new Interest();
		in.type = (byte)0xc1;
		for(int i=0;i<iv6.next.size();i++){
			NextHeader ng = iv6.next.get(i);
			if(ng.code==(byte)0x06){
				byte[] x = new byte[2];
				x[0]=ng.data[2];
				x[1]=ng.data[3];
				in.setPort(ByteArrayUtil.byteArrayToShort(x));
			}
			if(ng.code==(byte)0x35){
				byte[] x = new byte[2];
				x[0]=ng.data[4];
				x[1]=ng.data[5];
				in.setPid(ByteArrayUtil.byteArrayToShort(x));
				x[0]=ng.data[6];
				x[1]=ng.data[7];
				in.setFlag(ByteArrayUtil.byteArrayToShort(x));
			}
		}
		in.setName(iv6.src);
		return in;
	}
	
	public static ContentObject parseToContentObject(IPv6Packet iv6){
		ContentObject co = new ContentObject();
		co.type = (byte)0xc2;
		for(int i=0;i<iv6.next.size();i++){
			NextHeader ng = iv6.next.get(i);
			if(ng.code==(byte)0x35){
				co.setVersion(ng.data[3]);
				byte[] x = new byte[2];
				x[0]=ng.data[4];
				x[1]=ng.data[5];
				co.setPid(ByteArrayUtil.byteArrayToShort(x));
				x[0]=ng.data[6];
				x[1]=ng.data[7];
				co.setTotalpid(ByteArrayUtil.byteArrayToShort(x));
			}
		}
		co.setName(iv6.src);
		co.setData(iv6.payload);
		return co;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static byte[] creatInterestPacket(String name,int pid,int port,int flag){
		IPv6Packet packet = new IPv6Packet();
		NextHeader nh = new NextHeader();
		byte[] opt = new byte[6];       //type(1) version(1) pid(2) flag(2)服务承载网
		opt[0] = (byte)0xc1;
		opt[1] = (byte)0;
		byte[] s = ByteArrayUtil.shortToByteArray(pid);
		opt[2] = s[0];  opt[3] = s[1];
		s = ByteArrayUtil.shortToByteArray(flag);
		opt[4] = s[0];  opt[5] = s[1];
		nh.BuildNextHeader((byte)0x35, opt);
		packet.addNextheader(nh);

		s = ByteArrayUtil.shortToByteArray(port);
		opt[0]=s[0];opt[1]=s[1];
		opt[2]=s[0];opt[3]=s[1];
		nh = new NextHeader();
		nh.BuildNextHeader((byte)0x06, opt);
		packet.addNextheader(nh);
		
		packet.src = name;
		return packet.getIpv6Packet("ccn");	
	}
	

	private static byte[] creatDataPacket(String name,int version,int totalpid, int pid,byte[] data){
		IPv6Packet packet = new IPv6Packet();
		NextHeader nh = new NextHeader();
		byte[] opt = new byte[6];       //type(1) version(1) pid(2) totalpid(2)
		opt[0] = (byte)0xc2;
		opt[1] = (byte)version;
		byte[] s = ByteArrayUtil.shortToByteArray(pid);


		opt[2] = s[0];  opt[3] = s[1];
		s = ByteArrayUtil.shortToByteArray(totalpid);
		opt[4] = s[0];  opt[5] = s[1];
		nh.BuildNextHeader((byte)0x35, opt);
		packet.addNextheader(nh);
		
		packet.src = name;
		packet.payload = data;
		return packet.getIpv6Packet("ccn");	
	}
	
	
	/*public static void main(String[] args) {
		Interest in = new Interest("/pku/ccn/svn", 1, 5000,7);
		byte[] temp = PacketTools.InterestToByte(in);  //名称 、 块号、 初始请求的端口  、 服务承载网
		IPv6Packet iv6 = new IPv6Packet(temp);
		Interest in2 = PacketTools.parseToInterest(iv6);
		System.out.println(in2.getName()+"  "+in2.getPid()+"  "+in2.getPort()+"  "+in2.getFlag());
		
		ContentObject co2 = new ContentObject("/pku/cc/svn", 0, 567, 7, "123456789".getBytes());
		temp = PacketTools.ContentToByte(co2);//名称、版本号、总块号、块号、内容
		iv6 = new IPv6Packet(temp);
		ContentObject co = PacketTools.parseToContentObject(iv6);
		System.out.println(co.getName()+"  "+co.getPid()+"  "+co.getTotalpid()+"  "+co.getVersion()+"  "+new String(co.getData()));
		
		temp = PacketTools.creatDataPacket("/pku/cc/parse", 4, 120, 12, "1234567777".getBytes());//名称、版本号、总块号、块号、内容
		
		CCNData info = PacketTools.parse(temp);
		if(info==null)return;
		else if(info.type==(byte)0xc1){
			in = (Interest)info;
			System.out.println(in.getName()+"  "+in.getPid()+"  "+in.getPort()+"  "+in.getFlag());
		}
		else if(info.type==(byte)0xc2){
			co = (ContentObject)info;
			System.out.println(co.getName()+"  "+co.getPid()+"  "+co.getTotalpid()+"  "+co.getVersion()+"  "+new String(co.getData()));
		}
	}*/
}
