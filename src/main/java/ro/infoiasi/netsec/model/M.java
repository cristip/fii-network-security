package ro.infoiasi.netsec.model;

import java.math.BigInteger;

public class M {
	
	public static BigInteger fromChar(char m){
		return m == '0'?BigInteger.ZERO:BigInteger.ONE;
	}
	
	public static BigInteger fromInt(int m){
		return m == 0?BigInteger.ZERO:BigInteger.ONE;
	}
	
	public static BigInteger fromBoolean(Boolean m){
		return m?BigInteger.ONE:BigInteger.ZERO;
	}
	
	public static Boolean isValid(BigInteger m){
		return m.equals(BigInteger.ZERO) || m.equals(BigInteger.ONE);
	}
}
