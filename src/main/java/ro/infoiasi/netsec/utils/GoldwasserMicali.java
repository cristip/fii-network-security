package ro.infoiasi.netsec.utils;

import java.math.BigInteger;

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
		while(sb.length() < 1024){
			sb.insert(0, 0);
		}
		return sb.toString();
	}
	
	public String encrypt(String value) throws NumberFormatException, InputException{
		char[] chars = value.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		for(char c:chars){
			String bs = Integer.toBinaryString(c);
			while(bs.length() < Byte.SIZE){
				bs = "0"+bs;
			}
			sb.append(bs);
		}
		StringBuffer out = new StringBuffer();
		for(int i = 0; i < sb.length(); i++){
			String encrypted = encrypt(Integer.parseInt(String.valueOf(sb.charAt(i))));
			out.append(encrypted);
		}
		return out.toString();
	}
	public String decryptText(String value) throws InputException{
		int rem = value.length()%gen.getLength();
		if(rem != 0){
			String message = "Improper input size: " + rem;
			logger.error(message);
			throw new InputException(message);
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i =0; i < value.length(); i+=gen.getLength()){
			BigInteger c = new BigInteger(value.substring(i, i + gen.getLength()), 2);
			int res = decrypt(c).intValue();
			sb.append(res);
		}
		
		int steps = sb.length()/Byte.SIZE;
		byte[] bytes = new byte[steps];
		for(int i = 0; i < steps; i++){
			bytes[i] = Byte.parseByte(sb.substring(i*Byte.SIZE, (i+1)*Byte.SIZE), 2);
		}
		return new String(bytes);
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
	 * decryption: using the factorization p, q determine 
	 * if the bytes are a quadratic residue (m <- 0) or not (m <- 1)
	 * @param bytes to be evaluated
	 * @return
	 */
	public BigInteger decrypt(BigInteger c){
		//logger.info("got encrypted input: " + bytes.length);
		if(gen.isQRn(c)){
			return BigInteger.ZERO;
		}
		return BigInteger.ONE;
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
			return decrypt(bi).toString();
		}catch(StringIndexOutOfBoundsException e){
			throw new InputException("StringIndexOutOfBoundsException " +e.getMessage());
		}catch(NumberFormatException e){
			throw new InputException("NumberFormatException " + e.getMessage());
		}
	}
}
