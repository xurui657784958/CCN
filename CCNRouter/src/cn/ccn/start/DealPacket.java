package cn.ccn.start;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Set;

import cn.ccn.ctrl.CtrlProtocol;
import cn.ccn.model.ContentObject;
import cn.ccn.model.Interest;
import cn.ccn.model.protocol.Face;
import cn.ccn.model.protocol.FibItem;
import cn.ccn.model.protocol.PitItem;
import cn.ccn.packet.IPv6Packet;
import cn.ccn.packet.PacketTools;
import cn.ccn.routing.ContentManager;
import cn.ccn.routing.FaceManager;
import cn.ccn.routing.FibManager;
import cn.ccn.routing.PitManager;
import cn.pku.util.GlobalVar;
import cn.pku.util.NameUtil;
import cn.pku.util.PacketSender;

public class DealPacket implements Runnable{
	DatagramPacket packet;
	IPv6Packet ipack;
	public DealPacket(DatagramPacket packet, DatagramSocket socket) {
		this.packet = packet;
	}
	
	public void unlockPacket(String ip,int port, byte[] data){
		ipack = new IPv6Packet(data);
		if(ipack.type == (byte)0xc1){
			new CCNPacketHandler(packet.getAddress().getHostAddress(),ipack,(byte)0xc1).handlerInterest();
		}
		if(ipack.type == (byte)0xc2){
			new CCNPacketHandler(packet.getAddress().getHostAddress(),ipack,(byte)0xc2).handlerContentObject();
		}
	}
	
	@Override
	public void run() {
		unlockPacket(packet.getAddress().getHostAddress(), packet.getPort(), packet.getData());
	}
}


class CCNPacketHandler{

	public String ip;
	public Interest fin;
	public ContentObject fco;
	
	public CCNPacketHandler(String ip,IPv6Packet ipack,byte flag){
		this.ip = ip;
		if(flag==(byte)0xc1)fin = PacketTools.parseToInterest(ipack);
		if(flag==(byte)0xc2){
			fco = PacketTools.parseToContentObject(ipack);
			if(fco.getVersion()==(byte)0xff){
//				RouterStatistics.contentRegister++;
//				TransferToICNController.transferNameLSA(fco.getName()+"/SN="+fco.getPid());
//				fco.setVersion(0);
			}
			
		}
		

	}
	
	public void handlerInterest(){
		System.out.println("handle interest" + fin.getName()+":"+fin.getPid());
		String csname = NameUtil.contentStoreName(fin.getName(), fin.getPid());
		String pitname = NameUtil.distinctName(fin.getName(), fin.getPid(),fin.getFlag());
		String fibname = pitname;

		//check whether the interest in carry
		boolean isin = false;
		for(int i=0;i<GlobalVar.icnCarry.size();i++){
			if(GlobalVar.icnCarry.get(fin.getFlag())!=null)isin = true;
		}	
		if(fin.getFlag()==0)isin=true;
		if(!isin){
			return;
		}
		
		//-----------------------------------
		
		Face face = FaceManager.isFaceExist(ip, fin.getPort());
		
		if(face==null){
			face = FaceManager.addFace(ip, fin.getPort());
		}
		ContentObject co = ContentManager.getContent(csname, fin.getName(), fin.getPid());
		if(co!=null){
			byte[] buf = PacketTools.ContentToByte(co);
			PacketSender.send(face, buf);
			return;
		}else{
			System.out.println("interest : "+csname +" no content to match");
		}
		
		PitItem pit = new PitItem(pitname, face.getFaceID());
		PitManager.addPit(pit);
			
		//check fib
		FibItem fib = FibManager.getFib(fibname);
		
		if(fib!=null){
			Interest in = new Interest();
			in.setName(fin.getName());
			in.setPid(fin.getPid());
			in.setPort(GlobalVar.routerPort);
			in.setFlag(fin.getFlag());
			Iterator<Integer>it = fib.getWaitingFaces().iterator();
			while(it.hasNext()){
				face = FaceManager.getFace(it.next());
				byte[] buf = PacketTools.InterestToByte(in);
				PacketSender.send(face, buf);
			}
		}else{
			new CtrlProtocol().transferContentFib(fin.getName(), fin.getPid(), fin.getFlag());
		}
		
	}
	public void handlerContentObject(){
		System.out.println("handle content");
		ContentObject co = fco;
		
		//update cs
		ContentManager.addContent(co.getName()+"/SN="+co.getPid(),co);
		
		//check pit
		Set<Integer> set = GlobalVar.icnCarry.keySet();
		for(Integer i : set){
			PitItem pit = PitManager.getPit(co.getName()+"/SN="+co.getPid()+"/F="+i);
			
			if(pit!=null){
				Iterator<Integer>it = pit.getWaitingFaces().iterator();
				Face face;
				while(it.hasNext()){
					face = FaceManager.getFace(it.next());
					byte[] buf = PacketTools.ContentToByte(co);
					PacketSender.send(face, buf);
				}
				PitManager.delPit(co.getName()+"/SN="+co.getPid()+"/F="+i);
			}else{
				System.out.println("load pit:"+null+" ********"+co.getName()+"/SN="+co.getPid()+"/F="+i);
			}
		}
	}

}