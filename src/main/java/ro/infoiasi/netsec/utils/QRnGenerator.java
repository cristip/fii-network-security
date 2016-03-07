package ro.infoiasi.netsec.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.log4j.Logger;


public class QRnGenerator {
	
	final static Logger logger = Logger.getLogger(QRnGenerator.class);
	
	private BigInteger N;
	private BigInteger P;
	private BigInteger Q;
	private BigInteger Y;
	
	private byte[] lambdaSeed;
	
	private static final String DEFAULT_LAMBDA = "Lambda implicit";
	private static final int DEFAULT_LENGTH = 1024;
	private SecureRandom rnd;
	
	private int length;

	private static final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
	
	public QRnGenerator(){
		this.lambdaSeed =  DEFAULT_LAMBDA.getBytes();
		this.setLength(DEFAULT_LENGTH);
		this.init();
	}
	
	public QRnGenerator(byte[] lambda, int byteLength) {
		this.lambdaSeed = lambda;
		this.setLength(byteLength);
		this.init();
	}
	
	private void init(){
		rnd = new SecureRandom(lambdaSeed);
		RSAGen();
		Y = getNewNonQRn();
		logger.info("init P, Q, N, Y: " + P.toString() + ", " + Q.toString() + ", " + N.toString());
	}
	
	private void RSAGen(){
		int halfLength = Math.abs(this.getLength()/2);
		P = BigInteger.probablePrime(halfLength, rnd);
		Q = BigInteger.probablePrime(halfLength, rnd);
		N = P.multiply(Q);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public boolean isQRn(BigInteger a){
		return a.modPow(P.subtract(BigInteger.ONE).divide(TWO ), P).equals(BigInteger.ONE) &&
				a.modPow(Q.subtract(BigInteger.ONE).divide(TWO ), Q).equals(BigInteger.ONE); 
	}
	
	public BigInteger getNewNonQRn(){
		if(null == N){
			logger.error("Eroare la generare NQRn: init");
			return null;
		}
		while(true){
			BigInteger result = getNewFromZn();
			if( !isQRn(result) ){
				return result;
			}
		}
	}
	
	public BigInteger getNewFromZn(){
		if(null == N){
			logger.error("Eroare la generare Zn: init");
			return null;
		}
		while(true){
			BigInteger result = rng(length/8);
			if(result.compareTo(N) < 0){
				return result;
			}
		}
	}
	
	public BigInteger rng(int byteLength){
		while(true){
			byte[] bytes = new byte[byteLength];
			rnd.nextBytes(bytes);
			BigInteger result = new BigInteger(bytes);
			if(result.compareTo(BigInteger.ZERO) > 0){
				return result;
			}
		}
	}

	/**
	 * @return the n
	 */
	public BigInteger getN() {
		return N;
	}

	/**
	 * @return the p
	 */
	public BigInteger getP() {
		return P;
	}

	/**
	 * @return the q
	 */
	public BigInteger getQ() {
		return Q;
	}
	
	/**
	 * @return the y
	 */
	public BigInteger getY() {
		return Y;
	}
}
