package accessibleToEveryOne;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import other.Getter;

public class ViewStudentTotalAttendance extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		out.print("<html><head><title>View Attendance</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		String rollNumber=request.getParameter("rollNumber");
		String dateFrom=request.getParameter("dateFrom");
		String dateTo=request.getParameter("dateTo");
 
		String sqlDateTo=null;
		String sqlDateFrom=null;
	
		DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");  
        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd");  
        
        try 
        	{
        		java.util.Date utilDateTo = userDateFormat.parse(dateTo);
        		sqlDateTo = dateFormatNeeded.format(utilDateTo);
        		java.util.Date utilDateFrom = userDateFormat.parse(dateFrom);
        		sqlDateFrom=dateFormatNeeded.format(utilDateFrom);
	 		
		try{	
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

				PreparedStatement stmt11=con.prepareStatement("SELECT student_id,course_id FROM student_info where roll_number=?");
				stmt11.setString(1, rollNumber);
				ResultSet r11=stmt11.executeQuery();

				if(r11.next())
				{
					String studentId=r11.getString(1);
					String courseId=r11.getString(2);
					
					showTheoryAttendance(studentId,courseId,sqlDateTo,sqlDateFrom,response);
					showPracticalAttendance(studentId,courseId,sqlDateTo,sqlDateFrom,response);
				}
				else 
				{	
					out.println("Enter Valid Enrollment Number");
					RequestDispatcher rd=request.getRequestDispatcher("viewStudentTotalAttendance.jsp");
					rd.include(request, response);
					return;
				}
			}catch(Exception e)
				{	    
					out.print("<script>alert('Unable To View Attendance');</script>");
					System.out.println("Exception by View Total Attendance "+e);
					RequestDispatcher rd=request.getRequestDispatcher("viewStudentTotalAttendance.jsp");
					rd.include(request, response);
				}	
			
        	}catch (ParseException e) 
			{
			out.print("<script>alert('Enter Valid Date');</script>");
			System.out.println("Exception by View Student Total Attendance"+e);
			RequestDispatcher rd=request.getRequestDispatcher("viewStudentTotalAttendance.jsp");
			rd.include(request, response);
			}
        out.print("<a href='loginPage.jsp'>Home</a>");
		out.println("</body></head></html>");
	}

	private void showPracticalAttendance(String studentId,
			String courseId, String sqlDateTo, String sqlDateFrom, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
		
		PrintWriter out=response.getWriter();

		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

		PreparedStatement stmt=con.prepareStatement("SELECT subject_id,name FROM subject_info where type='P' and course_Id=?");
		stmt.setString(1, courseId);
		ResultSet rset=stmt.executeQuery();
		
		out.print("Practical");
		out.println("<table><tr>");
		while(rset.next())
				{  
					String subjectId=rset.getString(1);
					String subjectName=rset.getString(2);
					out.print("<td align='center'><a href='ViewSubjectAttendanceOfStudent?subjectId="+subjectId+"&studentId="+studentId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(subjectName)+"</a><br>");
					
					PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and date between ? and ? group by student_id");
					ps.setString(1, subjectId);
					ps.setString(2, sqlDateFrom);
					ps.setString(3, sqlDateTo);
					ResultSet rps=ps.executeQuery();
					
					if(rps.next()) 
						{
							out.print("<b>"+rps.getString(1)+"</b></td>");
						}
				}
		out.print("</tr>");

		PreparedStatement stmt1=con.prepareStatement("SELECT subject_id FROM subject_info where type='P' and course_Id=?");
		stmt1.setString(1, courseId);
		ResultSet rset1=stmt1.executeQuery();

		out.print("<tr>");
		while(rset1.next())
				{  
					String subjectId=rset1.getString(1);
					
					PreparedStatement stmt2=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and date between ? and ?");
					stmt2.setString(1, studentId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet r2=stmt2.executeQuery();

					while(r2.next())
						{
							out.print("<td align='center'>"+r2.getString(1));
						}

				}
		out.print("</tr>");
		out.print("</table>");
			}

	private void showTheoryAttendance(String studentId, 
			String courseId, String sqlDateTo, String sqlDateFrom, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
		PrintWriter out=response.getWriter();

		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

		PreparedStatement stmt=con.prepareStatement("SELECT subject_id,name FROM subject_info where type='T' and course_Id=?");
		stmt.setString(1, courseId);
		ResultSet rset=stmt.executeQuery();
		
		out.print("Theory");
		out.println("<table><tr>");
		while(rset.next())
				{  
					String subjectId=rset.getString(1);
					String subjectName=rset.getString(2);
					out.print("<td align='center'><a href='ViewSubjectAttendanceOfStudent?subjectId="+subjectId+"&studentId="+studentId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(subjectName)+"</a><br>");
					
					PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and date between ? and ? group by student_id");
					ps.setString(1, subjectId);
					ps.setString(2, sqlDateFrom);
					ps.setString(3, sqlDateTo);
					ResultSet rps=ps.executeQuery();
					
					if(rps.next()) 
						{
							out.print("<b>"+rps.getString(1)+"</b></td>");
						}
				}
		out.print("</tr>");

		PreparedStatement stmt1=con.prepareStatement("SELECT subject_id FROM subject_info where type='T' and course_Id=?");
		stmt1.setString(1, courseId);
		ResultSet rset1=stmt1.executeQuery();

		out.print("<tr>");
		while(rset1.next())
				{  
					String subjectId=rset1.getString(1);
					
					PreparedStatement stmt2=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and date between ? and ?");
					stmt2.setString(1, studentId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet r2=stmt2.executeQuery();

					while(r2.next())
						{
							out.print("<td align='center'>"+r2.getString(1));
						}

				}
		out.print("</tr>");
		out.print("</table>");
	}

}
