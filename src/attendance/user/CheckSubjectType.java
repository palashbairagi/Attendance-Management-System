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


public class CheckSubjectType extends HttpServlet {
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
	    String userId=request.getParameter("userId");

		out.println("<html><head><title>Select time</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("</head><body>");
		
	    try{
	    	Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
	
			PreparedStatement stmt=con.prepareStatement("SELECT type FROM subject_info where subject_id=?");
			stmt.setString(1, subjectId);
			ResultSet rset=stmt.executeQuery();
			
			if(rset.next())
			{
				if(rset.getString(1).equals("P"))
				{
					out.print("<form action='SelectGroup'>");
				//	RequestDispatcher rd=request.getRequestDispatcher("SelectGroup?subjectId="+subjectId+"&date="+date+"&userId="+userId);
				//	rd.forward(request, response);
				}
				else 
				{
					out.print("<form action='AddTheoryAttendancePage'>");
				//	RequestDispatcher rd=request.getRequestDispatcher("AddTheoryAttendancePage?subjectId="+subjectId+"&date="+date+"&userId="+userId);
				//	rd.forward(request, response);
				}
			}
			
			PreparedStatement stmt1=con.prepareStatement("SELECT semester from course_info where course_id=(select course_id from subject_info where subject_id=?)");
			stmt1.setString(1, subjectId);
			ResultSet rset1=stmt1.executeQuery();
			
			if(rset1.next())
			{	 
				out.print("<table>");
				out.print("<input type='hidden' name='subjectId' value='"+subjectId+"'>");
				out.print("<input type='hidden' name='date' value='"+date+"'>");
				out.print("<input type='hidden' name='userId' value='"+userId+"'>");
				
				String semester=rset1.getString(1);
				
				if((semester).equals("3rd") || (semester).equals("4th"))
				{
					out.print("<tr><td>Time</td><td><select name='time'>");
					out.print("<option value='09:30:00'>09:30</option>");
					out.print("<option value='10:20:00'>10:20</option>");
					out.print("<option value='11:10:00'>11:10</option>");
					out.print("<option value='12:30:00'>12:30</option>");
					out.print("<option value='01:20:00'>01:20</option>");
					out.print("<option value='02:10:00'>02:10</option>");
					out.print("<option value='03:00:00'>03:00</option>");
					out.print("</select></td></tr>");
				}
				else
				{
					out.print("<tr><td>Time</td><td><select name='time'>");
					out.print("<option value='08:00:00'>08:00</option>");
					out.print("<option value='08:50:00'>08:50</option>");
					out.print("<option value='09:40:00'>09:40</option>");
					out.print("<option value='11:00:00'>11:00</option>");
					out.print("<option value='11:50:00'>11:50</option>");
					out.print("<option value='12:40:00'>12:40</option>");
					out.print("<option value='01:30:00'>01:30</option>");
					out.print("</select></td></tr>");
				}
				out.println("<tr><td colspan='2' align='center'><input type='submit' value='Next'></td></tr>");
				out.print("</table>");
			}
	    }catch(Exception e)
		{
			System.out.println("Exception by Check Subject Type "+e);
			RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAddAttendance");
			rd.forward(request, response);
		}
	    out.print("<a href='userLogin.jsp'>Home</a>");
		out.println("</form></body></html>");
	}
}
