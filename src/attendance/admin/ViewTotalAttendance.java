package attendance.admin;

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

public class ViewTotalAttendance extends HttpServlet {
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
		
		out.print("<html><head><title>View Attendance</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");

		String courseId=request.getParameter("courseId");
		String type=request.getParameter("type");
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
				if(type.equals("T"))showTheoryAttendance(courseId,sqlDateTo,sqlDateFrom,response);
				else showPracticalAttendance(courseId,sqlDateTo,sqlDateFrom,response);
			}catch(Exception e)
				{	    
					out.print("<script>alert('Unable To View Attendance');</script>");
					System.out.println("Exception by View Total Attendance "+e);
					RequestDispatcher rd=request.getRequestDispatcher("SelectCourseToViewAttendance");
					rd.include(request, response);
				}	
			
		out.print("</table>");
        
        	}catch (ParseException e) 
			{
			out.print("<script>alert('Enter Valid Date');</script>");
			System.out.println("Exception by View Total Attendance "+e);
			RequestDispatcher rd=request.getRequestDispatcher("SelectCourseToViewAttendance");
			rd.include(request, response);
			}
		out.println("</body></head></html>");
	}

	private void showPracticalAttendance(String courseId,
			String sqlDateTo, String sqlDateFrom, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException{
		PrintWriter out=response.getWriter();
		out.print("<table><tr><td>Practical</tr></table>");
		showGroupAAttendance(courseId,sqlDateTo,sqlDateFrom,response);
		showGroupBAttendance(courseId,sqlDateTo,sqlDateFrom,response);
	}

	private void showGroupBAttendance(String courseId,
			String sqlDateTo, String sqlDateFrom, HttpServletResponse response)throws IOException, ClassNotFoundException, SQLException {
		PrintWriter out=response.getWriter();

		String rollNumber=null;
		String studentName=null;
		String subjectId=null;
		String studentId=null;
		
		boolean loop=false;
		boolean printStudentName=true;

		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

		PreparedStatement stmt11=con.prepareStatement("select subject_id from subject_info where type='P' and course_id=? order by name");
		stmt11.setString(1, courseId);
		ResultSet r11=stmt11.executeQuery();
		
		out.print("<table>");
		out.print("<tr><td>Group B</tr>");
		
		if(r11.next())
			{
			out.print("<tr><td>Enrollment Number<td>Name");
			subjectId=r11.getString(1);
			
			PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
			stmt3.setString(1, subjectId);
			ResultSet r3=stmt3.executeQuery();
			
			while(r3.next())
				{	
					out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
					
					PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and group_name='B' and date between ? and ? group by student_id");
					ps.setString(1, subjectId);
					ps.setString(2, sqlDateFrom);
					ps.setString(3, sqlDateTo);
					ResultSet rps=ps.executeQuery();
					
					if(rps.next()) 
						{
							out.print("<b>"+rps.getString(1)+"<b></td>");
						}
				}
			}

		while(r11.next())
			{	
				subjectId=r11.getString(1);
				PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
				stmt3.setString(1, subjectId);
				ResultSet r3=stmt3.executeQuery();
				
				while(r3.next())
					{	
						out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
						PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and group_name='B' and date between ? and ? group by student_id");
						ps.setString(1, subjectId);
						ps.setString(2, sqlDateFrom);
						ps.setString(3, sqlDateTo);
						ResultSet rps=ps.executeQuery();
						
						if(rps.next())
							{
								out.print("<b>"+rps.getString(1)+"</b></td>");
							}
					}
			}
		PreparedStatement stmt=con.prepareStatement("SELECT roll_number,name,student_id FROM view_attendance_by_subject where course_id=? and group_name='B' group by student_id");
		stmt.setString(1, courseId);
		ResultSet r=stmt.executeQuery();
				
		while(r.next())
			{
				loop=true;
				rollNumber=r.getString(1);
				studentName=r.getString(2);
				studentId=r.getString(3);
				printStudentName=true;
		
				PreparedStatement stmt1=con.prepareStatement("select subject_id from subject_info where type='P' and course_id=? order by name");
				stmt1.setString(1, courseId);
				ResultSet r1=stmt1.executeQuery();
		
				while(r1.next())
					{
						subjectId=r1.getString(1);
						if(printStudentName)
							{
								out.print("<tr><td>"+rollNumber+"<td>"+studentName);
								printStudentName=false;
							}
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
			}
		if(loop==false)
			{
				out.print("<tr><td>No attendance</td></tr>");
			}		
	}

	private void showGroupAAttendance(String courseId,
			String sqlDateTo, String sqlDateFrom, HttpServletResponse response)throws IOException, ClassNotFoundException, SQLException {
		PrintWriter out=response.getWriter();

		String rollNumber=null;
		String studentName=null;
		String subjectId=null;
		String studentId=null;
		
		boolean loop=false;
		boolean printStudentName=true;

		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

		PreparedStatement stmt11=con.prepareStatement("select subject_id from subject_info where type='P' and course_id=? order by name");
		stmt11.setString(1, courseId);
		ResultSet r11=stmt11.executeQuery();
		
		out.print("<table>");
		out.print("<tr><td>Group A</tr>");
		
		if(r11.next())
			{
			out.print("<tr><td>Enrollment Number<td>Name");
			subjectId=r11.getString(1);
			
			PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
			stmt3.setString(1, subjectId);
			ResultSet r3=stmt3.executeQuery();
			
			while(r3.next())
				{	
					out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
					
					PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by student_id");
					ps.setString(1, subjectId);
					ps.setString(2, sqlDateFrom);
					ps.setString(3, sqlDateTo);
					ResultSet rps=ps.executeQuery();
					
					if(rps.next()) 
						{
							out.print("<b>"+rps.getString(1)+"</b></td>");
						}
				}
			}

		while(r11.next())
			{	
				subjectId=r11.getString(1);
				PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
				stmt3.setString(1, subjectId);
				ResultSet r3=stmt3.executeQuery();
				
				while(r3.next())
					{	
						out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
						PreparedStatement ps=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by student_id");
						ps.setString(1, subjectId);
						ps.setString(2, sqlDateFrom);
						ps.setString(3, sqlDateTo);
						ResultSet rps=ps.executeQuery();
						
						if(rps.next())
							{
								out.print("<b>"+rps.getString(1)+"</b></td>");
							}
					}
			}
		PreparedStatement stmt=con.prepareStatement("SELECT roll_number,name,student_id FROM view_attendance_by_subject where course_id=? and group_name='A' group by student_id");
		stmt.setString(1, courseId);
		ResultSet r=stmt.executeQuery();
				
		while(r.next())
			{
				loop=true;
				rollNumber=r.getString(1);
				studentName=r.getString(2);
				studentId=r.getString(3);
				printStudentName=true;
		
				PreparedStatement stmt1=con.prepareStatement("select subject_id from subject_info where type='P' and course_id=? order by name");
				stmt1.setString(1, courseId);
				ResultSet r1=stmt1.executeQuery();
		
				while(r1.next())
					{
						subjectId=r1.getString(1);
						if(printStudentName)
							{
								out.print("<tr><td>"+rollNumber+"<td>"+studentName);
								printStudentName=false;
							}
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
			}
		if(loop==false)
			{
				out.print("<tr><td>No attendance</td></tr>");
			}		
	}

	private void showTheoryAttendance(String courseId,String sqlDateTo, String sqlDateFrom,HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException
	{
	PrintWriter out=response.getWriter();

	String rollNumber=null;
	String studentName=null;
	String subjectId=null;
	String studentId=null;
	
	boolean loop=false;
	boolean printStudentName=true;

	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

	PreparedStatement stmt11=con.prepareStatement("select subject_id from subject_info where type='T' and course_id=? order by name");
	stmt11.setString(1, courseId);
	ResultSet r11=stmt11.executeQuery();
	
	if(r11.next())
		{
		out.print("<table>");
		out.print("<tr><td>Theory</tr>");
		out.print("<tr><td>Enrollment Number<td>Name");
		subjectId=r11.getString(1);
		
		PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
		stmt3.setString(1, subjectId);
		ResultSet r3=stmt3.executeQuery();
		
		while(r3.next())
			{	
				out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
				
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
		}

	while(r11.next())
		{	
			subjectId=r11.getString(1);
			PreparedStatement stmt3=con.prepareStatement("select name from subject_info where subject_id=?");
			stmt3.setString(1, subjectId);
			ResultSet r3=stmt3.executeQuery();
			
			while(r3.next())
				{	
					out.print("<td align='center'><a href='ViewAllSubjectAttendance?subjectId="+subjectId+"&dateFrom="+sqlDateFrom+"&dateTo="+sqlDateTo+"'>"+Getter.getShortName(r3.getString(1))+"</a><br>");
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
		}
	PreparedStatement stmt=con.prepareStatement("SELECT roll_number,name,student_id FROM view_attendance_by_subject where course_id=? group by student_id");
	stmt.setString(1, courseId);
	ResultSet r=stmt.executeQuery();
			
	while(r.next())
		{
			loop=true;
			rollNumber=r.getString(1);
			studentName=r.getString(2);
			studentId=r.getString(3);
			printStudentName=true;
	
			PreparedStatement stmt1=con.prepareStatement("select subject_id from subject_info where type='T' and course_id=? order by name");
			stmt1.setString(1, courseId);
			ResultSet r1=stmt1.executeQuery();
	
			while(r1.next())
				{
					subjectId=r1.getString(1);
					if(printStudentName)
						{
							out.print("<tr><td>"+rollNumber+"<td>"+studentName);
							printStudentName=false;
						}
					PreparedStatement stmt2=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and date between ? and ?");
					stmt2.setString(1, studentId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet r2=stmt2.executeQuery();
			
					while(r2.next())
						{
							out.print("<b><td align='center'>"+r2.getString(1)+"</b>");
						}
				}
			out.print("</tr>");
		}
	if(loop==false)
		{
			out.print("No attendance");
		}
	out.print("</table>");
	out.print("<br><br><a href='adminLogin.jsp'>Home</a>");
	out.print("</body></html>");
	}

}
