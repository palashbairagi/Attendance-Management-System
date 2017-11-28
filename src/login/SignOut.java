package login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		boolean isValid=other.Getter.isSessionValid(request);
		if(!isValid)
			{
				RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
				rd.forward(request, response);							
			}
		
		HttpSession session = request.getSession();
		String userId=null;
		String userName = null;
		String password=null;
		userId=(String)session.getAttribute("userId");
		userName=(String)session.getAttribute("userName");
		password=(String) session.getAttribute("password");
		
		if(session!=null || userName!=null || password!=null || userId!=null){
			session.removeAttribute("userId");
			session.removeAttribute("userName");
			session.removeAttribute("password");
			session.invalidate(); 
		}

		RequestDispatcher rd=request.getRequestDispatcher("/");
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
