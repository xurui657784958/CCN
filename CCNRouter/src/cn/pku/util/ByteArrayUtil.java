package cn.pku.util;
//modify
public class ByteArrayUtil {
	public static byte[] intToByteArray(int res) {
		byte[] targets = new byte[4];
		targets[3] = (byte) (res & 0xff);// ����? 
		targets[2] = (byte) ((res >> 8) & 0xff);// �ε�λ 
		targets[1] = (byte) ((res >> 16) & 0xff);// �θ�λ 
		targets[0] = (byte) (res >>> 24);// ����?,�޷�����ơ�? 
		return targets; 
	} 
	public static int byteArrayToInt(byte[] res) { 
		// һ��byte�������?24λ���?0x??000000��������8λ���?0x00??0000 
		int targets = (res[3] & 0xff) | ((res[2] << 8) & 0xff00)
		| ((res[1] << 24) >>> 8) | (res[0] << 24); 
		return targets; 
	} 
	
	public static byte[] shortToByteArray(int res){
		byte[] targets = new byte[2];	
		targets[1] = (byte) (res & 0xff);
		targets[0] = (byte) (res >> 8 & 0xff);
		return targets;
	}
	
	public static int byteArrayToShort(byte[] res){
		int targets = (res[1] & 0xff)| (res[0] << 8 & 0xff00);
		return targets;
	}
}
