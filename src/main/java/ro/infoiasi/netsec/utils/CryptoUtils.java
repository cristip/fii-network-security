package ro.infoiasi.netsec.utils;

import org.apache.log4j.Logger;

import ro.infoiasi.netsec.exception.InputException;

public class CryptoUtils {
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	final static Logger logger = Logger.getLogger(CryptoUtils.class);
	
	public static String bytes2Binary(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(byte b:bytes){
			sb.append(Integer.toBinaryString(b));
		}
		return sb.toString();
	}
	
	public static String bytes2HexPP2(byte[] bytes){
//		if(bytes[0] == 0){
//			bytes = Arrays.copyOfRange(bytes, 1, bytes.length-1);
//		}
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
	
	public static byte[] binary2Bytes(String value) throws InputException, StringIndexOutOfBoundsException, NumberFormatException {
		if(null == value){
			return null;
		}
		int inputLength = value.length();
		if(inputLength % Byte.SIZE != 0){
			throw new InputException("binary2Bytes: the string is not a multiple of " + Byte.SIZE);
		}
		int targetLength = inputLength/Byte.SIZE;
		byte[] res = new byte[targetLength];
		for(int i = 0; i < targetLength; i++){
			String currentBinarySegment = value.substring(i * Byte.SIZE, (i + 1) * Byte.SIZE );
			try{
				res[i] = Byte.parseByte(currentBinarySegment, 2);
			}catch(NumberFormatException e){
				String message = "Fatal: parsing byte at segment " + i + ": " + currentBinarySegment;
				logger.error(message);
				throw e;
			}
		}
		return res;
	}
}
