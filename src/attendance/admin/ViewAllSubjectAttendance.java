package attendance.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewAllSubjectAttendance extends HttpServlet {
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
		String sqlDateFrom=request.getParameter("dateFrom");
		String sqlDateTo=request.getParameter("dateTo");
		boolean type=false;

		out.print("<html><head><title>View Attendance</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
				
	try{		
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
			PreparedStatement ps=con.prepareStatement("select type from subject_info where subject_id=?");
    		ps.setString(1, subjectId);
    		ResultSet rst=ps.executeQuery();
    		
    		if(rst.next())
    		{
    			type=rst.getString(1).equals("T");
    		}
    		
        	if(type){showTheoryAttendance(subjectId,sqlDateFrom,sqlDateTo,response);}
        	else{showGroupAAttendance(subjectId,sqlDateFrom,sqlDateTo,response);
        		showGroupBAttendance(subjectId,sqlDateFrom,sqlDateTo,response);}	
        	
		}catch(Exception e)
			{
				out.print("<script>alert('Unable To View Attendance');</script>");
				System.out.println("Exception by View All Subject Attendance "+e);
				RequestDispatcher rd1=request.getRequestDispatcher("SelectSubjectToView");
				rd1.include(request, response);
			}	
		out.println("</body></head></html>");
	}

private void showGroupBAttendance(String subjectId, String sqlDateFrom, String sqlDateTo, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
PrintWriter out=response.getWriter();
	
	String date=null;
	String time=null;
	String studentId=null;
	boolean displayStudentName=true;
	boolean recordExists = false;
	String attendance="";
	String color="";
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
	
	PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='B' and date between ? and ? group by date,time");
	stmt.setString(1, subjectId);
	stmt.setString(2, sqlDateFrom);
	stmt.setString(3, sqlDateTo);
	ResultSet rs=stmt.executeQuery();
	
	out.print("<table>");
	out.print("<tr><td>Group B<tr>");
	out.print("<th width='150' align='center'>Enrollment No <th>Name<th>");
	
	while(rs.next())
		{ 	
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
							
				out.print("<th>"+day+"-"+month+"<br>("+time+")");
		}
	out.print("<th>Total");		
			
	PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM View_attendance_by_subject where subject_id=? and group_name='B' group by student_id ");
	stmt1.setString(1, subjectId);
	ResultSet r=stmt1.executeQuery();
	
	while(r.next())
		{
				PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='B' and date between ? and ? group by date,time");
				stmt2.setString(1, subjectId);
				stmt2.setString(2, sqlDateFrom);
				stmt2.setString(3, sqlDateTo);
				ResultSet rs2=stmt2.executeQuery();
				
				out.print("<tr>");
			
				while(rs2.next())
					{ 	
							date=rs2.getString(1);
							time=rs2.getString(2);
							studentId=r.getString(1);
							
							PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and subject_id=? and date=? and time=? and group_name='B'");
							stmt3.setString(1, studentId);
							stmt3.setString(2, subjectId);
							stmt3.setString(3, date);
							stmt3.setString(4, time);
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
	out.print("<tr><td>Total</td><td></td><td></td>");
	
	PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='B' and date between ? and ? group by date,time");
	stmt5.setString(1, subjectId);
	stmt5.setString(2, sqlDateFrom);
	stmt5.setString(3, sqlDateTo);
	ResultSet rs5=stmt5.executeQuery();
		
	while(rs5.next())
		{
				date=rs5.getString(1);
				time=rs5.getString(2);
				PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and date=? and time=? and attendance='P' and group_name='B'");
				stmt6.setString(1, subjectId);
				stmt6.setString(2, date);
				stmt6.setString(3, time);
				ResultSet rs6=stmt6.executeQuery();
		
				if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
		}
	out.print("</tr>");
	
	if(recordExists==false)
		{
			out.print("No Attendance");
		}
}

private void showGroupAAttendance(String subjectId, String sqlDateFrom, String sqlDateTo, HttpServletResponse response)  throws IOException, ClassNotFoundException, SQLException {
	PrintWriter out=response.getWriter();
	
	String date=null;
	String time=null;
	String studentId=null;
	boolean displayStudentName=true;
	boolean recordExists = false;
	String attendance="";
	String color="";
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
	
	PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by date,time");
	stmt.setString(1, subjectId);
	stmt.setString(2, sqlDateFrom);
	stmt.setString(3, sqlDateTo);
	ResultSet rs=stmt.executeQuery();
	
	out.print("<table>");
	out.print("<tr><td>Group A<tr>");
	out.print("<th width='150' align='center'>Enrollment No <th>Name<th>");
	
	while(rs.next())
		{ 	
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
							
				out.print("<th>"+day+"-"+month+"<br>("+time+")");
		}
	out.print("<th>Total");		
		
	PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM View_attendance_by_subject where subject_id=? and group_name='A' group by student_id ");
	stmt1.setString(1, subjectId);
	ResultSet r=stmt1.executeQuery();
	
	while(r.next())
		{
				PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by date,time");
				stmt2.setString(1, subjectId);
				stmt2.setString(2, sqlDateFrom);
				stmt2.setString(3, sqlDateTo);
				ResultSet rs2=stmt2.executeQuery();
				
				out.print("<tr>");
			
				while(rs2.next())
					{ 	
							date=rs2.getString(1);
							time=rs2.getString(2);
							studentId=r.getString(1);
							
							PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and subject_id=? and date=? and time=? and group_name='A'");
							stmt3.setString(1, studentId);
							stmt3.setString(2, subjectId);
							stmt3.setString(3, date);
							stmt3.setString(4, time);
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
	out.print("<tr><td>Total</td><td></td><td></td>");
	
	PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and group_name='A' and date between ? and ? group by date,time");
	stmt5.setString(1, subjectId);
	stmt5.setString(2, sqlDateFrom);
	stmt5.setString(3, sqlDateTo);
	ResultSet rs5=stmt5.executeQuery();
		
	while(rs5.next())
		{
				date=rs5.getString(1);
				time=rs5.getString(2);
				PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and date=? and time=? and attendance='P' and group_name='A'");
				stmt6.setString(1, subjectId);
				stmt6.setString(2, date);
				stmt6.setString(3, time);
				ResultSet rs6=stmt6.executeQuery();
		
				if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
		}
	out.print("</tr>");
	
	if(recordExists==false)
		{
			out.print("No Attendance");
		}
}

private void showTheoryAttendance(String subjectId, String sqlDateFrom, String sqlDateTo, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
	PrintWriter out=response.getWriter();
	
	String date=null;
	String time=null;
	String studentId=null;
	boolean displayStudentName=true;
	boolean recordExists = false;
	String attendance="";
	String color="";
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
	
	PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and date between ? and ? group by date,time");
	stmt.setString(1, subjectId);
	stmt.setString(2, sqlDateFrom);
	stmt.setString(3, sqlDateTo);
	ResultSet rs=stmt.executeQuery();
					
	out.print("<table>");
	out.print("<th width='150' align='center'>Enrollment No <th>Name<th>");
	
	while(rs.next())
		{ 	
				recordExists=true;
				date=rs.getString(1);
				time=rs.getString(2);
				int c1,c2; String day,month;
				c1=date.indexOf("-");
				c2=date.indexOf("-",c1+1);
				month=date.substring(c1+1,c2);
				day=date.substring(c2+1);
		
				out.print("<th>"+day+"-"+month+"<br>("+time+")");
		}
	out.print("<th>Total");		
		
	PreparedStatement stmt1=con.prepareStatement("SELECT student_id FROM attendance_info where subject_id=? group by student_id ");
	stmt1.setString(1, subjectId);
	ResultSet r=stmt1.executeQuery();
	
	while(r.next())
		{
				PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and date between ? and ? group by date,time");
				stmt2.setString(1, subjectId);
				stmt2.setString(2, sqlDateFrom);
				stmt2.setString(3, sqlDateTo);
				ResultSet rs2=stmt2.executeQuery();
				
				out.print("<tr>");
			
				while(rs2.next())
					{ 	
							date=rs2.getString(1);
							time=rs2.getString(2);
							studentId=r.getString(1);
							
							PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and subject_id=? and date=? and time=?");
							stmt3.setString(1, studentId);
							stmt3.setString(2, subjectId);
							stmt3.setString(3, date);
							stmt3.setString(4, time);
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
	out.print("<tr><td>Total</td><td></td><td></td>");
	
	PreparedStatement stmt5=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and date between ? and ? group by date,time");
	stmt5.setString(1, subjectId);
	stmt5.setString(2, sqlDateFrom);
	stmt5.setString(3, sqlDateTo);
	ResultSet rs5=stmt5.executeQuery();
		
	while(rs5.next())
		{
				date=rs5.getString(1);
				time=rs5.getString(2);
				PreparedStatement stmt6=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where subject_id=? and date=? and time=? and attendance='P'");
				stmt6.setString(1, subjectId);
				stmt6.setString(2, date);
				stmt6.setString(3, time);
				ResultSet rs6=stmt6.executeQuery();
		
				if(rs6.next())out.print("<td align='center'><b>"+rs6.getString(1)+"</b></td>");
		}
	out.print("</tr>");
	
	if(recordExists==false)
		{
			out.print("No Attendance");
		}
	out.print("</table>");
	out.print("<a href='loginPage.jsp'>Home</a>");
	out.print("</body></html>");
	}
}

