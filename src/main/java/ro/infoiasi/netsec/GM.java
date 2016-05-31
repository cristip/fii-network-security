package ro.infoiasi.netsec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ro.infoiasi.netsec.exception.InputException;
import ro.infoiasi.netsec.utils.GoldwasserMicali;

@WebServlet("/GM")
public class GM extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519648183226251148L;
	final static Logger logger = Logger.getLogger(GM.class);
	
	//public void init() throws ServletException{
	//	GoldwasserMicali.getInstance();
	//}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e){
			logger.error("failed to set the character encoding to UTF-8:\n" + e.getMessage());
		}
		response.addHeader("Content-Type", "text/html; charset=utf-8");
		String bit = null;//request.getParameter("bit");
		//String cryptoBit = request.getParameter("ctext");
		String cryptoBit = null;
		String plainText = null;//request.getParameter("plain");
		//String cryptText = request.getParameter("crypto");
		String cryptText = null;
		
		BufferedReader br = request.getReader();
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String jsonString = sb.toString();
		try{
			logger.info("parsing content " + jsonString.substring(0, jsonString.length()> 50?50:jsonString.length()));
			JSONObject jo = new JSONObject(jsonString); //HTTP.toJSONObject(jsonString);
			logger.info("got json object");
			if(jo.has("crypto")){
				cryptText = jo.getString("crypto");
			}else if(jo.has("ctext")){
				cryptoBit = jo.getString("ctext");
			}else if(jo.has("bit")){
				bit = jo.getString("bit");
			}else if(jo.has("plain")){
				plainText = jo.getString("plain");
			}
		}catch(JSONException e){
			logger.error("error parsing json ", e);
		}
		
		
		GoldwasserMicali gm = GoldwasserMicali.getInstance();
		String result = null;
		if(null != plainText){
			try {
				long startTime = System.nanoTime();
				result = gm.encrypt(plainText);
				logger.info("encrypted " + plainText + " in " + (System.nanoTime() - startTime)/1000000 + " ms");
			} catch (NumberFormatException | InputException e) {
				response.sendError(400, e.getMessage());
				return;
			}
		}else if(null != cryptText){
			try{
				long startTime = System.nanoTime();
				result = gm.decryptText(cryptText);
				logger.info("decrypted " + result + " in " + (System.nanoTime() - startTime)/1000000 + " ms");
			}catch(InputException e){
				response.sendError(400, e.getMessage());
				return;
			}catch(Exception e){
				logger.error(e.getMessage());
				e.printStackTrace();
				response.sendError(500, e.getMessage());
				return;
			}
		}else if(null != bit){
			try{
				int m = Integer.parseInt(bit);
				result = gm.encrypt(m);
				
			}catch(NumberFormatException | InputException e){
				response.sendError(400, "Bad request: bit must be 0 or 1. Was: " + bit);
				return;
			}
		}else if(null != cryptoBit){
			try{
				result = gm.decrypt(cryptoBit);
			}catch(InputException e){
				response.sendError(400, e.getMessage());
				return;
			}
		}
		if(null == result){
			response.sendError(400, "Empty input.");
			return;
		}
		response.getWriter().append(result);
		response.getWriter().flush();
		
	}

}
