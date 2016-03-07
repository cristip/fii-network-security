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
	
	public void init() throws ServletException{
		GoldwasserMicali.getInstance();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bit = request.getParameter("bit");
		try{
			int m = Integer.parseInt(bit);
			GoldwasserMicali gm = GoldwasserMicali.getInstance();
			String result = gm.encrypt(m);
			response.getWriter().append(result);
			response.getWriter().flush();
		}catch(NumberFormatException | InputException e){
			response.sendError(400, "Bad request: bit must be 0 or 1. Was: " + bit);
		}
	}

}
