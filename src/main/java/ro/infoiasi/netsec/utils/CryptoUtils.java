package ro.infoiasi.netsec.utils;

public class CryptoUtils {
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	
	public static String bytes2HexPP(byte[] bytes){
		char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = ' ';
	        hexChars[j * 3 + 1] = hexArray[v >>> 4];
	        hexChars[j * 3 + 2] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
