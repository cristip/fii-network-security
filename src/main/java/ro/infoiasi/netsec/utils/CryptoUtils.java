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
	    return new String(hexChars).substring(1);
	}
	public static byte[] hex2bytes(String hexChars) throws Exception{
		if(null == hexChars){
			return null;
		}
		hexChars = hexChars.replaceAll("\\s+","");
		int length = hexChars.length();
		if(length%2 != 0){
			throw new Exception("Odd input for hex2chars");
		}
		byte[] result = new byte[length/2];
		for(int i = 0; i < length; i+=2){
			result[i/2] = (byte) ((Character.digit(hexChars.charAt(i), 16) << 4) + Character.digit(hexChars.charAt(i+1), 16));

		}
		return result;
	}
}
