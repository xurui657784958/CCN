package cn.ccn.packet;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ccn.model.ContentObject;
import cn.ccn.model.Interest;
import cn.pku.util.ByteArrayUtil;

public class IPv6Packet {
	public byte[] header;
	public byte firstcode;
	public List<NextHeader> next;
	public byte[] payload;   //实际数据（不包括拓展首部）
	public String src;
	public String dst;
	public byte type;
	public int packetLength;
	
	public IPv6Packet() {
		next = new ArrayList<NextHeader>();
		firstcode = 0x3c;
	}
	
	public void addNextheader(NextHeader nh){
		if(next.size()==0){
			firstcode = nh.code;
		}else{
			next.get(next.size()-1).data[0] = nh.code;
		}
		next.add(nh);
	}
	
	public IPv6Packet(byte[] data) {
		header =  Arrays.copyOfRange(data, 0, 40);
		int payloadlength = ByteArrayUtil.byteArrayToShort(Arrays.copyOfRange(data, 4, 6));
		next  = new ArrayList<NextHeader>();
		byte code = data[6],pcode;
		firstcode = code;
		int pos = 40;		
		int length;
		String flag = null;
		int i = 0;
		while(code!=(byte)0x3c){
			pcode = code;
			code = data[pos];
			length = data[pos+1]&0xff;
			NextHeader nh = new NextHeader();
			nh.RecvNextHeader(pcode, Arrays.copyOfRange(data, pos, pos+length));
			next.add(nh);
			if(nh.code==(byte)0x35){
				type = nh.data[2];
				if(type == (byte)0xc1||type == (byte)0xc2)flag = "ccn";
			}
			pos = pos+length;		
		}
		payload = Arrays.copyOfRange(data, pos, 40+payloadlength);
		packetLength = 40+payloadlength;
		if(flag!=null&&flag.equals("ccn")){
			src = new String (Arrays.copyOfRange(data, 8, 40)).trim();
		}else{
			try {
				src = Inet6Address.getByAddress(Arrays.copyOfRange(data, 8, 24)).getHostAddress();
				dst = Inet6Address.getByAddress(Arrays.copyOfRange(data, 24, 40)).getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] getIpv6Packet(String type){
		if(type.equals("ccn")){
			byte[] buf = null;
			int nextheaderlength = 0;
			for(int i=0;i<next.size();i++){
				nextheaderlength+=next.get(i).data.length;
			}
			int length;
			if(payload!=null)length = nextheaderlength+payload.length;
			else 
				length = nextheaderlength;		
			buf = new byte[40+length];
			buf[6] = firstcode;  // nextheader
			buf[7] = 0x40;
			byte[] temp = new byte[2];
			temp = ByteArrayUtil.shortToByteArray(length);
			buf[4] = temp[0];buf[5] = temp[1];
			byte[] srcByte = null;
			byte[] dstByte = null;
			if(src!=null)srcByte = src.getBytes();
			if(src!=null)System.arraycopy(srcByte, 0, buf, 8, srcByte.length);
	// complete the ipv6 header
			int pos = 40;
			for(int i=0;i<next.size();i++){
				System.arraycopy(next.get(i).data, 0, buf, pos, next.get(i).data.length);
				pos = pos + next.get(i).data.length;
			}
	// complete the extended ipv6 header		
			if(payload!=null)System.arraycopy(payload, 0, buf, pos, payload.length);
			return buf;
		}else{
			return getIpv6Packet();
		}
	}
	
	public byte[] getIpv6Packet(){
		byte[] buf = null;
		int nextheaderlength = 0;
		for(int i=0;i<next.size();i++){
			nextheaderlength+=next.get(i).data.length;
		}
		int length;
		if(payload!=null)length = nextheaderlength+payload.length;
		else 
			length = nextheaderlength;		
		buf = new byte[40+length];
		buf[6] = firstcode;  // nextheader
		buf[7] = 0x40;
		byte[] temp = new byte[2];
		temp = ByteArrayUtil.shortToByteArray(length);
		buf[4] = temp[0];buf[5] = temp[1];
		byte[] srcByte = null;
		byte[] dstByte = null;
		try {
			if(src!=null)srcByte = Inet6Address.getByName(src).getAddress();
			if(dst!=null)dstByte = Inet6Address.getByName(dst).getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(src!=null)System.arraycopy(srcByte, 0, buf, 8, srcByte.length);
		if(dst!=null)System.arraycopy(dstByte, 0, buf, 24, dstByte.length);
// complete the ipv6 header
		int pos = 40;
		for(int i=0;i<next.size();i++){
			System.arraycopy(next.get(i).data, 0, buf, pos, next.get(i).data.length);
			pos = pos + next.get(i).data.length;
		}
// complete the extended ipv6 header		
		if(payload!=null)System.arraycopy(payload, 0, buf, pos, payload.length);
		return buf;
	}
	
	/*public static void main(String[] args) {
		byte[] buf = {
			(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,
			(byte)0x00,(byte)0x26,   //有效载荷长度（拓展首部+载荷之和  38）
			(byte)0x33,(byte)0x40,   //下一个首部    跳数64
			(byte)0x08,(byte)0x09,(byte)0x10,(byte)0x11,(byte)0x12,(byte)0x13,(byte)0x14,(byte)0x15,
			(byte)0x16,(byte)0x17,(byte)0x18,(byte)0x19,(byte)0x20,(byte)0x21,(byte)0x22,(byte)0x23,
			(byte)0x24,(byte)0x25,(byte)0x26,(byte)0x27,(byte)0x28,(byte)0x29,(byte)0x30,(byte)0x31,
			(byte)0x32,(byte)0x33,(byte)0x34,(byte)0x35,(byte)0x36,(byte)0x37,(byte)0x38,(byte)0x39,//header
			
			(byte)0x2c,(byte)0x0a,(byte)0x42,(byte)0x43,(byte)0x44,(byte)0x45,(byte)0x46,(byte)0x47,(byte)0x48,(byte)0x49,
			(byte)0x3c,(byte)0x0a,(byte)0x52,(byte)0x53,(byte)0x54,(byte)0x55,(byte)0x56,(byte)0x57,(byte)0x58,(byte)0x59,
			(byte)0x60,(byte)0x61,(byte)0x62,(byte)0x63,(byte)0x64,(byte)0x65,(byte)0x66,(byte)0x67,(byte)0x68,(byte)0x69,
			(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00
		};
		IPv6Packet packet = new IPv6Packet(buf);
		System.out.println(packet.payload.length);
		for(int i=0;i<packet.payload.length;i++){
			System.out.printf("%2x ",packet.payload[i]);
			if((i+1)%8==0)System.out.println();
		}
		System.out.println();
		byte[] opt = {0x20,0x30,0x40};
		NextHeader nh1 = new NextHeader();
		nh1.BuildNextHeader((byte) 0x2c, opt);
		NextHeader nh2 = new NextHeader();
		nh2.BuildNextHeader((byte) 0x33, opt);	
		System.out.println("---------------gx-----------");		
		IPv6Packet pk = new IPv6Packet();
		pk.src = "2001:250:3c02:200:3584:7d7f:ceac:0051";
		pk.dst = "2001:250:3c02:200:3584:7d7f:ceac:0052";
		pk.addNextheader(nh1);
		pk.addNextheader(nh2);
		pk.payload = opt;
		byte[] packbuf = pk.getIpv6Packet();
		
		for(int i=0;i<packbuf.length;i++){
			System.out.printf("%2x ",packbuf[i]);
			if((i+1)%8==0)System.out.println();
		}
		System.out.println();
		System.out.println("---------------------------");
		packet = new IPv6Packet(packbuf);
		System.out.println(packet.src);
		System.out.println(packet.dst);
		System.out.println(packet.firstcode);
		System.out.println(packet.next.size());
		/////////////////////////////////////////////////////////////
		byte type = 0;
		byte[] temp;
		IPv6Packet iv6;
		int dstport=-1;
		int srcport=-1;
		
		System.out.println("---------------interest-----------");
		Interest in2 = new Interest("/pku/ccn/svn", 1, 5000,7);
		temp = PacketTools.InterestToByte(in2);  //名称 、 块号、 初始请求的端口  、 服务承载网
		for(int i=0;i<temp.length;i++){
			System.out.printf("%2x ",temp[i]);
			if((i+1)%8==0)System.out.println();
		}
		System.out.println();
		iv6 = new IPv6Packet(temp);
		srcport=-1;
	
		int version = -1,pid = -1,flag=-1;
		for(int i=0;i<iv6.next.size();i++){
			NextHeader ng = iv6.next.get(i);
			if(ng.code==(byte)0x06){
				System.out.println("opt length:"+ng.data.length);
				byte[] x = new byte[2];
				x[0]=ng.data[2];
				x[1]=ng.data[3];
				srcport = ByteArrayUtil.byteArrayToShort(x);
				x[0]=ng.data[4];
				x[1]=ng.data[5];
				dstport = ByteArrayUtil.byteArrayToShort(x);
			}
			if(ng.code==(byte)0x35){
				System.out.println("opt length:"+ng.data.length);
				type = ng.data[2];
				version = ng.data[3];
				byte[] x = new byte[2];
				x[0]=ng.data[4];
				x[1]=ng.data[5];
				pid = ByteArrayUtil.byteArrayToShort(x);
				x[0]=ng.data[6];
				x[1]=ng.data[7];
				flag = ByteArrayUtil.byteArrayToShort(x);
			}
		}
		System.out.println(srcport+" "+dstport);
		System.out.println(iv6.src);
		System.out.println("version:"+version +"  pid:"+pid+"  flag:"+flag);
		System.out.printf("%x\n",type);
		
		
System.out.println("---------------content-----------");
		ContentObject co2 = new ContentObject("/pku/cc/svn", 0, 567, 7, "123456789".getBytes());
		temp = PacketTools.ContentToByte(co2);//名称、版本号、总块号、块号、内容

		for(int i=0;i<temp.length;i++){
			System.out.printf("%2x ",temp[i]);
			if((i+1)%8==0)System.out.println();
		}
		System.out.println();
		iv6 = new IPv6Packet(temp);
		srcport=-1;
		version = -1;
		pid=-1;
		int totalpid=-1;
		for(int i=0;i<iv6.next.size();i++){
			NextHeader ng = iv6.next.get(i);
//			if(ng.code==(byte)0x06){
//				System.out.println("opt length:"+ng.data.length);
//				byte[] x = new byte[2];
//				x[0]=ng.data[2];
//				x[1]=ng.data[3];
//				srcport = ByteArrayUtil.byteArrayToShort(x);
//				x[0]=ng.data[4];
//				x[1]=ng.data[5];
//				dstport = ByteArrayUtil.byteArrayToShort(x);
//			}
			if(ng.code==(byte)0x35){
				System.out.println("opt length:"+ng.data.length);
				version = ng.data[3];
				byte[] x = new byte[2];
				x[0]=ng.data[4];
				x[1]=ng.data[5];
				pid = ByteArrayUtil.byteArrayToShort(x);
				x[0]=ng.data[6];
				x[1]=ng.data[7];
				totalpid = ByteArrayUtil.byteArrayToShort(x);
			}
		}
//		System.out.println(srcport+" "+dstport);
		System.out.println(iv6.src);
		System.out.println("version:"+version +"  pid:"+pid+"  totalpid:"+totalpid);
		System.out.println(new String(iv6.payload));
		
//		
//		
//		
//		
//		String names = "/pku/netlab/katon.mp4/SN=1/F=3";
//		System.out.println(names.substring(names.indexOf("/F=")+3));
//		String[] info = names.split("/SN");
//		String[] t = info[1].substring(1).split("/F=");
//		System.out.println(t[0]+" "+t[1]);
	}//main*/
	
}
