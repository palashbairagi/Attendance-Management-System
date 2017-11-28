package accessibleToEveryOne;

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

import other.Getter;

public class ViewSubjectAttendanceOfStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String subjectId=request.getParameter("subjectId");
		String sqlDateFrom=request.getParameter("dateFrom");
		String sqlDateTo=request.getParameter("dateTo");
		String studentId=request.getParameter("studentId");
		
		String date=null;
		String time=null;
		boolean recordExists = false;
		String attendance="";
		String color="";
		
		out.print("<html><head><title>View Attendance</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		try{		
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
			PreparedStatement ps=con.prepareStatement("select * from subject_info where subject_id=?");
    		ps.setString(1, subjectId);
    		ResultSet rst=ps.executeQuery();
    		
    		if(rst.next())
    		{
    			out.print("<table align='center'><tr><td>Subject </td><td>"+rst.getString(2)+"</td></tr>");
    			out.print("</table>");
    		}
    		
    		PreparedStatement stmt=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and student_id=? and date between ? and ? group by date,time");
			stmt.setString(1, subjectId);
			stmt.setString(2, studentId);
			stmt.setString(3, sqlDateFrom);
			stmt.setString(4, sqlDateTo);
			ResultSet rs=stmt.executeQuery();
			
			out.print("<table>");
			
			if(rs.next())
				{
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
			
			while(rs.next() && recordExists)
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
			
			PreparedStatement stmt2=con.prepareStatement("SELECT date,time FROM view_attendance_by_subject where subject_id=? and student_id=? and date between ? and ? group by date,time");
			stmt2.setString(1, subjectId);
			stmt2.setString(2, studentId);
			stmt2.setString(3, sqlDateFrom);
			stmt2.setString(4, sqlDateTo);
			ResultSet rs2=stmt2.executeQuery();
			
			out.print("<tr>");
		
			while(rs2.next() && recordExists)
				{ 	
						date=rs2.getString(1);
						time=rs2.getString(2);
						
						PreparedStatement stmt3=con.prepareStatement("SELECT * FROM view_attendance_by_subject where student_id=? and subject_id=? and date=? and time=? ");
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
								out.print("<td align='center' style='color:"+color+"'>"+attendance+"</td>");
							}
				}
			PreparedStatement stmt4=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and subject_id=? and attendance='P' and date between ? and ?");
			stmt4.setString(1, studentId);
			stmt4.setString(2, subjectId);
			stmt4.setString(3, sqlDateFrom);
			stmt4.setString(4, sqlDateTo);
			ResultSet rs4=stmt4.executeQuery();
			
			if(rs4.next() && recordExists)
				{
				out.print("<td align='center'><b>"+rs4.getString(1)+"</b></td>");
				}

		
			if(recordExists==false)
			{
				out.print("<tr><td>No Attendance<br><br></td></tr>");
			}
		
		}catch(Exception e)
			{
				out.print("<script>alert('Unable To View Attendance');</script>");
				System.out.println("Exception by View Subject Attendance of Student "+e);
				RequestDispatcher rd=request.getRequestDispatcher("ViewStudentTotalAttendance");
				rd.include(request, response);
			}	
		out.print("</table>");
		out.print("<a href='loginPage.jsp'>Home</a>");
		out.println("</body></head></html>");
	}
}
