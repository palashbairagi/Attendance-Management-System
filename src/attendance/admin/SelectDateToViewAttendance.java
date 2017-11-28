package attendance.admin;

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

public class SelectDateToViewAttendance extends HttpServlet {
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
		
		out.println("<html><head><title>Select Course And Date</title>");
		out.print("<script src='jquery-1.9.1.js'></script>");
		out.print("<script src='jquery-1.10.2.js'></script>");
		out.print("<script src='jquery-ui.js'></script>");
		out.print("<script src='jquery.validate.min.js'></script>");
		out.print("<script src='bootstrap.min.js'></script>");
		out.print("<script src='selectDateToViewAttendance.js'></script>");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<link rel='stylesheet' href='jquery-ui.css'>");
		out.print("</head>");
		out.println("<form action='ViewAttendanceByDate' id='dayAttendance'>");
	
	try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

		PreparedStatement stmt=con.prepareStatement("select * from course_info order by semester, branch, section");
		ResultSet rset=stmt.executeQuery();				

		if(rset.next())
			{
				out.println("<table><tr><td>Course</td><td><select name='courseId'>");
				out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem </option>");
	
				while(rset.next())
					{
						out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem</option>");	
					}
				out.println("</select></td></tr>");
				out.println("<tr><td>Date</td><td><input type='text' id='date' class='datepicker' name='date'></td></tr>");
				out.println("<input type='hidden' name='click' value='noClick'>");
				out.println("<tr><td colspan='2' align='center'><input type='submit' value='Go'></td></tr></table>");
			}
		else
			{
				out.println("Please Add Course<br><br>");
			}
	}catch(Exception e)
		{
			out.print("<script>alert('Unable To Select Course');</script>");
			System.out.println("Exception by Select Date To View Attendance "+e);
			RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
			rd.forward(request, response);
		}
		out.println("</form>");
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.print("</body></html>");
	}
}
