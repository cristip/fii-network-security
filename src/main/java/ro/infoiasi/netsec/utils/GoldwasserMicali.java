package ro.infoiasi.netsec.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.log4j.Logger;

import ro.infoiasi.netsec.exception.InputException;
import ro.infoiasi.netsec.model.M;

public class GoldwasserMicali {
	
	final static Logger logger = Logger.getLogger(GoldwasserMicali.class);
	private QRnGenerator gen;
	@SuppressWarnings("unused")
	private static final String LAMBDA = "19800120";
	
	private static final GoldwasserMicali instance = new GoldwasserMicali();
	
	private GoldwasserMicali() {
		setup();
	}
	
	
	public static GoldwasserMicali getInstance(){
		return instance;
	}
	/**
	 * the setp phase
	 * establishes the p, q, n and y
	 */
	public void setup(){
		try{
			gen = new QRnGenerator();
		}catch(Exception e){
			logger.error(e);
		}
	}
	/**
	 * 
	 * @param m 0 or 1
	 * @return the encrypted byte array
	 * @throws InputException
	 */
	public BigInteger encrypt(BigInteger m) throws InputException{
		if(!M.isValid(m)){
			throw new InputException();
		}
		BigInteger x = gen.getNewFromZn();
		BigInteger c = x.modPow(new BigInteger("2"), gen.getN());
		//byte[] res = null;
		BigInteger res = null;
		if(m.equals(BigInteger.ZERO)){
			res = c;
		}else if(m.equals(BigInteger.ONE)){
			res = c.multiply(gen.getY()).mod(gen.getN());
		}else
		{
			logger.error("FATAL: got " + m.intValue() + " to be encrypted...");
			throw new InputException("Only 0 or 1 can be encrypted");
		}
		return res;
	}
	
	/**
	 * the encription methd
	 * @param m integer
	 * @return hex representation of output
	 * @throws InputException
	 */
	public String encrypt(int m) throws InputException{
		if(m != 0 && m != 1){
			throw new InputException("Only 0 or 1 are allowed as ints in this method");
		}
		BigInteger c = M.fromInt(m);
		String result = encrypt(c).toString(2);
		StringBuffer sb = new StringBuffer();
		sb.append(result);
		while(sb.length() < gen.getLength()){
			sb.insert(0, 0);
		}
		return sb.toString();
	}
	
	public String encrypt(String value) throws NumberFormatException, InputException, UnsupportedEncodingException{
		byte[] source;
		try {
			source = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UTF-8 not supported");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(byte b:source){
			String segment = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
			sb.append(segment);
		}
		StringBuffer out = new StringBuffer();
		for(int i = 0; i < sb.length(); i++){
			String encrypted = encrypt(Integer.parseInt(String.valueOf(sb.charAt(i))));
			out.append(encrypted);
		}
		return out.toString();
	}
	/**
	 * 
	 * @param value binary string to be decrypted
	 * @return unicode decrypted string
	 * @throws InputException
	 */
	public String decryptText(String value) throws InputException{
		logger.info("decrypting text...." );
		if(null == value || value.isEmpty()){
			return null;
		}
		int valueSize = value.length();
		int rem = valueSize%gen.getLength();
		if(rem != 0){
			String message = "Improper input size: " + rem;
			logger.error(message);
			throw new InputException(message);
		}
		int step = Byte.SIZE*gen.getLength();
		final int destinationSize = valueSize/step;
		byte destination[] = new byte[destinationSize];
		char[] encryptedText = value.toCharArray();
		for(int i = 0, index = 0; i < valueSize; i += step, index++){
			StringBuffer currentByte = new StringBuffer();
			for(int j = 0; j < Byte.SIZE; j++){
				int initPosition = i+j*gen.getLength();
				char[] segment = Arrays.copyOfRange(encryptedText, initPosition, initPosition + gen.getLength());
				BigInteger c = new BigInteger(new String(segment), 2);
				currentByte.append( decrypt(c) );
			}
			//Byte.parseByte will fail with unicode characters.
			destination[index] = (byte)Integer.parseInt(currentByte.toString(), 2);
		}
		
		try {
			if(Charset.isSupported("UTF-8")){
				String result = new String(destination, "UTF-8");
				return result;
			}
			logger.error("Encoding UTF-8 not supported on this system");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * decryption: using the factorization p, q determine 
	 * if the bytes are a quadratic residue (m <- 0) or not (m <- 1)
	 * @param bytes to be evaluated
	 * @return
	 */
	public char decrypt(BigInteger c){
		//logger.info("got encrypted input: " + bytes.length);
		if(gen.isQRn(c)){
			return '0';
		}
		return '1';
	}

	/**
	 * 
	 * @param cryptoText binary representation of the crrypto text
	 * @return
	 * @throws InputException
	 */
	public String decrypt(String cryptoText) throws InputException{
		logger.info("got encrypted input:" + cryptoText);
		try{
			BigInteger bi = new BigInteger(cryptoText, 2);
			return String.valueOf(decrypt(bi));
		}catch(StringIndexOutOfBoundsException e){
			throw new InputException("StringIndexOutOfBoundsException " +e.getMessage());
		}catch(NumberFormatException e){
			throw new InputException("NumberFormatException " + e.getMessage());
		}
	}
}
