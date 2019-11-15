package cn.ccn.packet;

public class NextHeader {
	public byte code;
	public byte[] data;   //xie+shou+opt
	
	public void BuildNextHeader(byte code, byte[] opt) {		
		BuildNextHeader(code, opt, (byte)0x3c);
	}
	
	public void BuildNextHeader(byte code, byte[] opt, byte ncode) {
		// TODO Auto-generated constructor stub
		this.code = code;
		data = new byte[2+opt.length];
		data[0] = ncode;
		data[1] = (byte)(opt.length+2);
		System.arraycopy(opt, 0, data, 2, opt.length);
	}
	
	public void RecvNextHeader(byte code, byte[] data) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.data = data;
	}
}
