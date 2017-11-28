package attendance.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SelectSubjectAndDateToViewAttendance extends HttpServlet {
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
		
		out.println("<html><head>");
		out.print("<script src='jquery-1.9.1.js'></script>");
		out.print("<script src='jquery-1.10.2.js'></script>");
		out.print("<script src='jquery-ui.js'></script>");
		out.print("<script src='jquery.validate.min.js'></script>");
		out.print("<script src='bootstrap.min.js'></script>");
		out.print("<script src='selectDateToViewAttendance.js'></script>");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<link rel='stylesheet' href='jquery-ui.css'>");
		out.print("<title>Select Subject</title></head><Body>");

		
		HttpSession session=request.getSession();
		String userId=(String) session.getAttribute("userId");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				PreparedStatement stmt=con.prepareStatement("SELECT * FROM subject_info where user_id=? order by name");
				stmt.setString(1, userId);
				ResultSet rset=stmt.executeQuery();				
			
				if(rset.next())
					{
						out.println("<form action='ViewAttendanceBySubject' id='viewStudentTotalAttendance'>");
						out.print("<input type='hidden' name='userId' value='"+userId+"'>");
						out.println("<table><tr><td>Subject</td><td><select name='subjectId' >");
						out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+"("+rset.getString(3)+")</option>");
					
						while(rset.next())
							{
								out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+"("+rset.getString(3)+")</option>");	
							}
						out.print("</select></td></tr>");
						out.print("<tr><td>Date From </td><td><input type='text' class='datepicker' name='dateFrom' id='dateFrom'></td></tr>");
						out.print("<tr><td>Date To </td><td><input type='text' class='datepicker' name='dateTo' id='dateTo'></td></tr>");
						out.println("<tr><td colspan='2' align='center'><input type='submit' value='Go'></td></tr></table></form>");
					}
				else
					{
						out.println("No Subject <br><br>");
					}
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Select');</script>");
					System.out.println("Exception by Select Subject And Date To View"+e);
					RequestDispatcher rd=request.getRequestDispatcher("userLogin.jsp");
					rd.forward(request, response);
				}
		out.print("<a href='userLogin.jsp'>Home</a>");
		out.println("</body></html>");
	}
}
