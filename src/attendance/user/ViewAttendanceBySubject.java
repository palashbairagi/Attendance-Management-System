package attendance.user;

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

public class ViewAttendanceBySubject extends HttpServlet {
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
		
		String userId=request.getParameter("userId");
		String subjectId=request.getParameter("subjectId");
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
	    
        		out.print("<html><head><title>View Attendance</title>");
        		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
        		out.print("<script type='text/javascript' src='validation.js'></script>");
        		out.print("</head><body>");
        		boolean type = false;
        try
        	{		
        	Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
    		
    		PreparedStatement ps=con.prepareStatement("select * from subject_info where subject_id=?");
    		ps.setString(1, subjectId);
    		ResultSet rst=ps.executeQuery();
    		
    		if(rst.next())
    		{
    			type=rst.getString(3).equals("T");
    			out.print("<table align='center'><tr><td>Subject </td><td>"+rst.getString(2)+"</td></tr>");
    			out.print("<tr><td>From</td><td>"+dateFrom+"</td></tr><tr><td>  To</td><td>"+dateTo+"</td></tr></table>");
    		}
    		
        	if(type){showTheoryAttendance(userId,subjectId,sqlDateTo,sqlDateFrom,response);}
        	else{showGroupAAttendance(userId,subjectId,sqlDateTo,sqlDateFrom,response);
        		showGroupBAttendance(userId,subjectId,sqlDateTo,sqlDateFrom,response);}	
        	
        	}catch(Exception e)
				{	    
					out.print("<script>alert('Unable To View Attendance');</script>");
					System.out.println("Exception by View Attendance By Subject "+e);
					RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectAndDateToViewAttendance");
					rd.include(request, response);
				}
        	
        	}catch (ParseException e) 
    			{
    			out.print("<script>alert('Enter Valid Date');</script>");
    			System.out.println("Exception by View Attendance By Subject"+e);
    			RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectAndDateToViewAttendance");
    			rd.include(request, response);
    			}

        	out.print("</table>");
        	out.print("<a href='userLogin.jsp'>Home</a>");
        	out.println("</body></html>");
	}
	
	private void showGroupAAttendance(String userId, String subjectId,String sqlDateTo, String sqlDateFrom,HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
		PrintWriter out=response.getWriter();
		
		boolean recordExists=false;
		boolean displayStudentName=true;
		String date=null;
		String time=null;
		String studentId=null;
		String attendance="";
		String color="";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
		PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='A' and date between ? and ? group by date,time");
		stmt.setString(1, userId);
		stmt.setString(2, subjectId);
		stmt.setString(3, sqlDateFrom);
		stmt.setString(4, sqlDateTo);
		ResultSet rs=stmt.executeQuery();

		out.print("<table>");
		out.print("<td>Group A<tr>");
		if(rs.next())
			{
				out.print("<th width='150' align='center'>Enrollment No <th>Name<th>");
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
							
				out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}
						
		while(rs.next())
			{ 	
					date=rs.getString(1);
					time=rs.getString(2);
					int c1,c2; String day,month;
					c1=date.indexOf("-");
					c2=date.indexOf("-",c1+1);
					month=date.substring(c1+1,c2);
					day=date.substring(c2+1);
								
					out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}
		PreparedStatement totalstmt=con.prepareStatement("select count(*) FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by student_id");
		totalstmt.setString(1, subjectId);
		totalstmt.setString(2, sqlDateFrom);
		totalstmt.setString(3, sqlDateTo);
		ResultSet totalrs=totalstmt.executeQuery();
		
		if(totalrs.next()&& recordExists)out.print("<th>Total<br>"+totalrs.getString(1));		
		
		PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM view_attendance_by_subject where subject_id=? and user_id=? and group_name='A' group by student_id ");
		stmt1.setString(1, subjectId);
		stmt1.setString(2, userId);
		ResultSet r=stmt1.executeQuery();

		while(r.next() && recordExists)
			{
					PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='A' and date between ? and ? group by date,time");
					stmt2.setString(1, userId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet rs2=stmt2.executeQuery();
					
					out.print("<tr>");
					
					while(rs2.next() && recordExists)
						{ 	
								date=rs2.getString(1);
								time=rs2.getString(2);
								studentId=r.getString(1);
								
								PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and user_id=? and subject_id=? and date=? and time=? and group_name='A'");
								stmt3.setString(1, studentId);
								stmt3.setString(2, userId);
								stmt3.setString(3, subjectId);
								stmt3.setString(4, date);
								stmt3.setString(5, time);
								ResultSet rs3=stmt3.executeQuery();
								
								while(rs3.next())
									{
									attendance = rs3.getString(5);
									if(attendance.equals("A"))color="red";
									else if(attendance.equals("P"))color="green";
									else color="yellow";
									if(displayStudentName){out.print("<td>"+rs3.getString(1)+"</td><td>"+rs3.getString(2)+"</td><td></td><td align='center' style='color:"+color+"'>"+attendance+"</td>");displayStudentName=false;}
									else out.print("<td align='center' style='color:"+color+"'>"+attendance+"</td>");
									}
						}
					displayStudentName=true;
					
					PreparedStatement stmt4=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and group_name='A' and date between ? and ?");
					stmt4.setString(1, studentId);
					stmt4.setString(2, subjectId);
					stmt4.setString(3, sqlDateFrom);
					stmt4.setString(4, sqlDateTo);
					ResultSet rs4=stmt4.executeQuery();
					
					if(rs4.next())
						{
						out.print("<td align='center'><b>"+rs4.getString(1)+"</b></td>");
						}
		
					out.print("</tr>");
			}
		if(recordExists)out.print("<tr><td>Total</td><td></td><td></td>");
		
		PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='A' and date between ? and ? group by date,time");
		stmt5.setString(1, userId);
		stmt5.setString(2, subjectId);
		stmt5.setString(3, sqlDateFrom);
		stmt5.setString(4, sqlDateTo);
		ResultSet rs5=stmt5.executeQuery();
			
		while(rs5.next() && recordExists)
			{
					date=rs5.getString(1);
					time=rs5.getString(2);
					PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='A' and date=? and time=? and attendance='P'");
					stmt6.setString(1, userId);
					stmt6.setString(2, subjectId);
					stmt6.setString(3, date);
					stmt6.setString(4, time);
					ResultSet rs6=stmt6.executeQuery();
			
					if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
			}
		out.print("</tr>");
		
		if(recordExists==false)
			{
				out.print("<tr><td>No Attendance<br><br></td></tr>");
			}

	}

	private void showGroupBAttendance(String userId, String subjectId,String sqlDateTo, String sqlDateFrom,HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
		PrintWriter out=response.getWriter();
		
		boolean recordExists=false;
		boolean displayStudentName=true;
		String date=null;
		String time=null;
		String studentId=null;
		String attendance="";
		String color="";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
		PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='B' and date between ? and ? group by date,time");
		stmt.setString(1, userId);
		stmt.setString(2, subjectId);
		stmt.setString(3, sqlDateFrom);
		stmt.setString(4, sqlDateTo);
		ResultSet rs=stmt.executeQuery();
		
		out.print("<table>");
		out.print("<td>Group B<tr>");
			
		if(rs.next())
			{
				out.print("<th width='150' align='center'>Enrollment No<th>Name<th>");
			
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
						
				out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}

		while(rs.next())
			{ 	
					date=rs.getString(1);
					time=rs.getString(2);
					int c1,c2; String day,month;
					c1=date.indexOf("-");
					c2=date.indexOf("-",c1+1);
					month=date.substring(c1+1,c2);
					day=date.substring(c2+1);
								
					out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}

		PreparedStatement totalstmt=con.prepareStatement("select count(*) FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by student_id");
		totalstmt.setString(1, subjectId);
		totalstmt.setString(2, sqlDateFrom);
		totalstmt.setString(3, sqlDateTo);
		ResultSet totalrs=totalstmt.executeQuery();
		
		if(totalrs.next() && recordExists)out.print("<th>Total<br>"+totalrs.getString(1));		
		
		PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM view_attendance_by_subject where subject_id=? and user_id=? and group_name='B' group by student_id ");
		stmt1.setString(1, subjectId);
		stmt1.setString(2, userId);
		ResultSet r=stmt1.executeQuery();
		
		while(r.next() && recordExists)
			{
					PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='B' and date between ? and ? group by date,time");
					stmt2.setString(1, userId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet rs2=stmt2.executeQuery();
					
					out.print("<tr>");
					
					while(rs2.next())
						{ 	
								date=rs2.getString(1);
								time=rs2.getString(2);
								studentId=r.getString(1);
								
								PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and user_id=? and subject_id=? and date=? and time=? and group_name='B'");
								stmt3.setString(1, studentId);
								stmt3.setString(2, userId);
								stmt3.setString(3, subjectId);
								stmt3.setString(4, date);
								stmt3.setString(5, time);
								ResultSet rs3=stmt3.executeQuery();
								
								while(rs3.next())
									{
									attendance = rs3.getString(5);
									if(attendance.equals("A"))color="red";
									else if(attendance.equals("P"))color="green";
									else color="yellow";
									if(displayStudentName){out.print("<td>"+rs3.getString(1)+"</td><td>"+rs3.getString(2)+"</td><td></td><td align='center' style='color:"+color+"'>"+attendance+"</td>");displayStudentName=false;}
									else out.print("<td align='center' style='color:"+color+"'>"+attendance+"</td>");
									}
						}
					displayStudentName=true;
					
					PreparedStatement stmt4=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and group_name='B' and date between ? and ?");
					stmt4.setString(1, studentId);
					stmt4.setString(2, subjectId);
					stmt4.setString(3, sqlDateFrom);
					stmt4.setString(4, sqlDateTo);
					ResultSet rs4=stmt4.executeQuery();
					
					if(rs4.next())
						{
						out.print("<td align='center'><b>"+rs4.getString(1)+"</b></td>");
						}
		
					out.print("</tr>");
			}
		if(recordExists)out.print("<tr><td>Total</td><td></td><td></td>");
		
		PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='B' and date between ? and ? group by date,time");
		stmt5.setString(1, userId);
		stmt5.setString(2, subjectId);
		stmt5.setString(3, sqlDateFrom);
		stmt5.setString(4, sqlDateTo);
		ResultSet rs5=stmt5.executeQuery();
			
		while(rs5.next() && recordExists)
			{
					date=rs5.getString(1);
					time=rs5.getString(2);
					PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where user_id=? and subject_id=? and group_name='B' and date=? and time=? and attendance='P'");
					stmt6.setString(1, userId);
					stmt6.setString(2, subjectId);
					stmt6.setString(3, date);
					stmt6.setString(4, time);
					ResultSet rs6=stmt6.executeQuery();
			
					if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
			}
		out.print("</tr>");
		
		if(recordExists==false)
			{
				out.print("<tr><td>No Attendance<br><br></td></tr>");
			}
	}

	private void showTheoryAttendance(String userId,String subjectId, String sqlDateTo, String sqlDateFrom,HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException
	{
		PrintWriter out=response.getWriter();
		
		boolean recordExists=false;
		boolean displayStudentName=true;
		String date=null;
		String time=null;
		String studentId=null;
		String attendance="";
		String color="";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
		PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and date between ? and ? group by date,time");
		stmt.setString(1, userId);
		stmt.setString(2, subjectId);
		stmt.setString(3, sqlDateFrom);
		stmt.setString(4, sqlDateTo);
		ResultSet rs=stmt.executeQuery();
						
		out.print("<table>");
		
		if(rs.next())
			{
				
				out.print("<th width='150' align='center'>Enrollment No<th>Name<th>");
			
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
				
				out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}
		
		while(rs.next())
			{ 	
					date=rs.getString(1);
					time=rs.getString(2);
					int c1,c2; String day,month;
					c1=date.indexOf("-");
					c2=date.indexOf("-",c1+1);
					month=date.substring(c1+1,c2);
					day=date.substring(c2+1);
								
					out.print("<th>"+day+"-"+month+"<br>("+Getter.getShortTime(time)+")");
			}
		PreparedStatement totalstmt=con.prepareStatement("select count(*) FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by student_id");
		totalstmt.setString(1, subjectId);
		totalstmt.setString(2, sqlDateFrom);
		totalstmt.setString(3, sqlDateTo);
		ResultSet totalrs=totalstmt.executeQuery();
		
		if(totalrs.next())out.print("<th>Total<br>"+totalrs.getString(1));		
		
		PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM attendance_info where subject_id=? and user_id=? group by student_id ");
		stmt1.setString(1, subjectId);
		stmt1.setString(2, userId);
		ResultSet r=stmt1.executeQuery();
		
		while(r.next() && recordExists)
			{
					PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and date between ? and ? group by date,time");
					stmt2.setString(1, userId);
					stmt2.setString(2, subjectId);
					stmt2.setString(3, sqlDateFrom);
					stmt2.setString(4, sqlDateTo);
					ResultSet rs2=stmt2.executeQuery();
					
					out.print("<tr>");
					
					while(rs2.next())
						{ 	
								date=rs2.getString(1);
								time=rs2.getString(2);
								studentId=r.getString(1);
								
								PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and user_id=? and subject_id=? and date=? and time=?");
								stmt3.setString(1, studentId);
								stmt3.setString(2, userId);
								stmt3.setString(3, subjectId);
								stmt3.setString(4, date);
								stmt3.setString(5, time);
								ResultSet rs3=stmt3.executeQuery();
								
								while(rs3.next())
									{
									attendance = rs3.getString(5);
									if(attendance.equals("A"))color="red";
									else if(attendance.equals("P"))color="green";
									else color="yellow";
									if(displayStudentName){out.print("<td>"+rs3.getString(1)+"</td><td>"+rs3.getString(2)+"</td><td></td><td align='center' style='color:"+color+"'>"+attendance+"</td>");displayStudentName=false;}
									else out.print("<td align='center' style='color:"+color+"'>"+attendance+"</td>");
									}
						}
					displayStudentName=true;
					
					PreparedStatement stmt4=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and date between ? and ?");
					stmt4.setString(1, studentId);
					stmt4.setString(2, subjectId);
					stmt4.setString(3, sqlDateFrom);
					stmt4.setString(4, sqlDateTo);
					ResultSet rs4=stmt4.executeQuery();
					
					if(rs4.next())
						{
						out.print("<td align='center'><b>"+rs4.getString(1)+"</b></td>");
						}
		
					out.print("</tr>");
			}
		if(recordExists)out.print("<tr><td>Total</td><td></td><td></td>");
		
		PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where user_id=? and subject_id=? and date between ? and ? group by date,time");
		stmt5.setString(1, userId);
		stmt5.setString(2, subjectId);
		stmt5.setString(3, sqlDateFrom);
		stmt5.setString(4, sqlDateTo);
		ResultSet rs5=stmt5.executeQuery();
			
		while(rs5.next() && recordExists)
			{
					date=rs5.getString(1);
					time=rs5.getString(2);
					PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where user_id=? and subject_id=? and date=? and time=? and attendance='P'");
					stmt6.setString(1, userId);
					stmt6.setString(2, subjectId);
					stmt6.setString(3, date);
					stmt6.setString(4, time);
					ResultSet rs6=stmt6.executeQuery();
			
					if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
			}
		out.print("</tr>");
		
		if(recordExists==false)
			{
				out.print("<tr><td>No Attendance<br><br></td></tr>");
			}
		}
}