package attendance.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SelectGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();

			boolean isValid=other.Getter.isSessionValid(request);
			if(!isValid)
				{
					RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
					rd.forward(request, response);							
				}
			
			String subjectId=request.getParameter("subjectId");
		    String date=request.getParameter("date");
		    String time=request.getParameter("time");
		    String userId=request.getParameter("userId");

			out.print("<html><title>Select Group</title>");
			out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
			out.print("<script type='text/javascript' src='validation.js'></script>");
			out.print("</head><body>");
			out.print("<form action='AddPracticalAttendancePage'>");
			out.print("<input type='hidden' name='subjectId' value='"+subjectId+"'>");
			out.print("<input type='hidden' name='date' value='"+date+"'>");
			out.print("<input type='hidden' name='time' value='"+time+"'>");
			out.print("<input type='hidden' name='userId' value='"+userId+"'>");
			out.print("<table><tr><td>Group Name</td><td><input type='radio' name='groupName' value='A' checked>Group A</td>");
			out.print("<td><input type='radio' name='groupName' value='B' >Group B</td></tr>");
			out.print("<tr><td colspan='2' align='center'><input type='submit' value='GO'></td></tr>");
			out.print("</table></form>");
			out.print("<a href='userLogin.jsp'>Home</a>");
			out.print("</body></html>");
	}
}
