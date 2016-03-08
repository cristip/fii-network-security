package ro.infoiasi.netsec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ro.infoiasi.netsec.exception.InputException;
import ro.infoiasi.netsec.utils.GoldwasserMicali;

@WebServlet("/GM")
public class GM extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519648183226251148L;
	
	//public void init() throws ServletException{
	//	GoldwasserMicali.getInstance();
	//}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bit = request.getParameter("bit");
		String cryptoText = request.getParameter("ctext");
		GoldwasserMicali gm = GoldwasserMicali.getInstance();
		String result = null;
		if(null != bit){
			try{
				int m = Integer.parseInt(bit);
				result = gm.encrypt(m);
				
			}catch(NumberFormatException | InputException e){
				response.sendError(400, "Bad request: bit must be 0 or 1. Was: " + bit);
			}
		}else if(null != cryptoText){
			try{
				result = gm.decrypt(cryptoText);
			}catch(InputException e){
				response.sendError(400, e.getMessage());
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
