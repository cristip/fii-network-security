package ro.infoiasi.netsec.utils;

import java.math.BigInteger;
import java.util.Date;

import org.apache.log4j.Logger;

import ro.infoiasi.netsec.exception.InputException;
import ro.infoiasi.netsec.model.M;

public class GoldwasserMicali {
	
	final static Logger logger = Logger.getLogger(GoldwasserMicali.class);
	private QRnGenerator gen;
	private static final String LAMBDA = "19800120";
	
	private static final GoldwasserMicali instance = new GoldwasserMicali();
	
	private GoldwasserMicali() {
		setup();
	}
	
	
	public static GoldwasserMicali getInstance(){
		return instance;
	}
	
	public void setup(){
		try{
			int byteLength = 1024;
			byte [] lambdaBytes = String.valueOf( new Date().getTime() ).getBytes();
			gen = new QRnGenerator(lambdaBytes, byteLength);
		}catch(Exception e){
			logger.error(e);
		}
	}
	public String encrypt(int m) throws InputException{
		BigInteger c = M.fromInt(m);
		return CryptoUtils.bytes2HexPP( encrypt(c) );
	}
	
	public byte[] encrypt(BigInteger m) throws InputException{
		if(!M.isValid(m)){
			throw new InputException();
		}
		BigInteger x = gen.getNewFromZn();
		BigInteger c = x.modPow(new BigInteger("2"), gen.getN());
		if(m.equals(BigInteger.ZERO)){
			return c.toByteArray();
		}
		// = gen.getY().modPow(m, gen.getN()).multiply(x.modPow(new BigInteger("2"), gen.getN()));
		return c.multiply(gen.getY()).mod(gen.getN()).toByteArray();
	}
	public BigInteger decrypt(byte[] bytes){
		BigInteger c = new BigInteger(bytes);
		if(gen.isQRn(c)){
			return BigInteger.ZERO;
		}
		return BigInteger.ONE;
	}


	public String decrypt(String cryptoText) throws InputException{
		try{
			byte[] bytes = CryptoUtils.hex2bytes(cryptoText);
			return decrypt(bytes).toString();
		}catch(Exception e){
			throw new InputException(e.getMessage());
		}
	}
}
