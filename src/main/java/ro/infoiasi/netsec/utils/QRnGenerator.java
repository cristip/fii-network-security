package ro.infoiasi.netsec.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;


public class QRnGenerator {
	
	final static Logger logger = Logger.getLogger(QRnGenerator.class);
	
	private BigInteger N;
	private BigInteger P;
	private BigInteger Q;
	private BigInteger Y;
	
	private byte[] lambdaSeed;
	
	private static final String DEFAULT_LAMBDA = "Lambda implicit: ";
	private static final int DEFAULT_LENGTH = 1024;
	private SecureRandom rnd;
	
	//private static final BigInteger TWO = new BigInteger("2");

	private static final int MAX_INIT_TRIES = 10;
	
	private int length;
	
	private int tries = 0;

	public QRnGenerator(){
		this.setLength(DEFAULT_LENGTH);
		this.init();
	}
	
	
	
	private void init(){
		this.tries ++;
		this.lambdaSeed =  (DEFAULT_LAMBDA + String.valueOf(tries) + "@" + String.valueOf( new Date().getTime() )) .getBytes();
		rnd = new SecureRandom(lambdaSeed);
		RSAGen();
		if(tries > MAX_INIT_TRIES){
			logger.error("Fatal Error: in " + tries + " tries: N is " + N.toByteArray().length);
			return;
		}
		if(N.toByteArray().length != 128){
			logger.info("try #" + this.tries +": N:"+N.toByteArray().length);
			init();
			return;
		}
		logger.info("SUCCESS: Completed in " + tries + " tries: N is " + N.toByteArray().length);
		Y = getNewNonQRn();
		logger.info("init P, Q, N, Y: " + P.toString().substring(0, 5) + ", " + Q.toString().substring(0, 5) + ", " + N.toString().substring(0, 5) + ", " + P.toString().substring(0, 5));
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
	/**
	 * This method tests if a is QRn mod n
	 * It's based on the following propeties:
	 *  - Let's note [a, n] the Jacobi symbol
	 *  i) 	for any odd prime p and andy positive integer a:
	 *   [a, p] = a ^ ((p-1)/2) mod p
	 *  ii) let n = p * q, p, q prime numbers 
	 *  and *  [a, n] = [a, p] [a, q]
	 *  
	 * @param a
	 * @return true if a is a quadratic residue mod N.
	 */
	public boolean isQRn(BigInteger a){
		
		//return a.modPow(P.subtract(BigInteger.ONE).divide(TWO ), P).equals(BigInteger.ONE) &
		//		a.modPow(Q.subtract(BigInteger.ONE).divide(TWO ), Q).equals(BigInteger.ONE);
		int jacobiP = IntegerFunctions.jacobi(a, P);
		int jacobiQ = IntegerFunctions.jacobi(a, Q);
		return (jacobiP == 1 && jacobiQ == 1);
	}
	/**
	 * returns a new element from Zn which is not a quadratic rezidue mod N
	 * @return
	 */
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
	/**
	 * returns a new element from Zn
	 * @return
	 */
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
	/**
	 * (pseudo) random number generator
	 * @param byteLength the byte length of the resulting output
	 * @return a positive random number
	 */
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
