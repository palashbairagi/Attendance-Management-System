package attendance.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddPracticalAttendancePage extends HttpServlet {
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
		
		out.println("<html><head><title>Attendance</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		String subjectId=request.getParameter("subjectId");
	    String date=request.getParameter("date");
	    String time=request.getParameter("time");
	    String userId=request.getParameter("userId");
	    String groupName=request.getParameter("groupName");
	    
	    String courseId=null;
	    String sqlDate = null;
	    
        DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");  
        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd");  
        
        try 
        	{
        		java.util.Date utilDate = userDateFormat.parse(date);
        		sqlDate = dateFormatNeeded.format(utilDate);
		
	    
	    try{		
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				
				PreparedStatement ps=con.prepareStatement("select subject_id from view_attendance_by_subject where date=? and time=? group by subject_id ");
				ps.setString(1,sqlDate);
				ps.setString(2,time);
				ResultSet rst=ps.executeQuery();
				
				if(rst.next())
				{
					PreparedStatement ps1=con.prepareStatement("select subject_id from view_attendance_by_subject where date=? and time=? and group_name=? group by subject_id ");
					ps1.setString(1,sqlDate);
					ps1.setString(2,time);
					ps1.setString(3,groupName);
					ResultSet rst1=ps1.executeQuery();
					if(rst1.next())
					{
						out.print("Attendance already inserted for this group at "+date+"  "+time);
						RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAddAttendance");
		    			rd.include(request, response);
		    			return;
					}
				}
				if(rst.next())
				{
				out.print("Attendance already inserted for "+date+"  "+time);
				RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAddAttendance");
    			rd.include(request, response);
    			return;
				}
				else
				{
				PreparedStatement pstmt=con.prepareStatement("select course_id from subject_info where subject_id=?");
				pstmt.setString(1, subjectId);
				ResultSet rs=pstmt.executeQuery();
				
				if (rs.next())courseId=rs.getString(1);
				
				PreparedStatement stmt=con.prepareStatement("select * from attendance_view where subject_id=? and group_name=?");
				stmt.setString(1, subjectId);
				stmt.setString(2, groupName);
				ResultSet r=stmt.executeQuery();
		
				if(r.next())
					{	
						int sNo;
						int i=0;
						out.print("<form action='AddAttendance'>");
						out.print("<input type='hidden' name='type' value='P'>");
						out.print("<input type='hidden' name='groupName' value='"+groupName+"'>");
						out.print("<table><tr><th>S.No.<th>Enrollment Number<th>Name<th>Attendance<tr>");
						sNo=i+1;
						out.print("<tr><td>"+sNo+"<input type='hidden' value='"+r.getString(1)+"' name='studentId"+i+"'>");
						out.print("<td>"+r.getString(2));
						out.print("<td>"+r.getString(3));
						out.print("<td><input type='radio' value='P' name='attendance"+i+"' checked>P");
						out.print("<input type='radio' value='A' name='attendance"+i+"'>A");
						out.print("<input type='radio' value='L' name='attendance"+i+"'>L</tr>");
			
						i=1;
						while(r.next())
							{
								sNo=i+1;
								out.print("<tr><td>"+sNo+"<input type='hidden' value='"+r.getString(1)+"' name='studentId"+i+"'>");
								out.print("<td>"+r.getString(2));
								out.print("<td>"+r.getString(3));
								out.print("<td><input type='radio' value='P' name='attendance"+i+"' checked>P");
								out.print("<input type='radio' value='A' name='attendance"+i+"'>A");
								out.print("<input type='radio' value='L' name='attendance"+i+"'>L</tr>");
								i++;
							}
						out.print("<tr><td><input type='submit' value='Ok' align='center'>");
						out.print("</table>");
						out.print("<input type='hidden' name='variable' value='"+i+"'>");
						out.print("<input type='hidden' name='date' value='"+sqlDate+"'>");
						out.print("<input type='hidden' name='time' value='"+time+"'>");
						out.print("<input type='hidden' name='subjectId' value='"+subjectId+"'>");
						out.print("<input type='hidden' name='courseId' value='"+courseId+"'>");
						out.print("<input type='hidden' name='userId' value='"+userId+"'>");
						out.print("<input type='hidden' name='groupName' value='"+groupName+"'>");
						out.print("<input type='hidden' name='type' value='P'>");
						out.print("</form>");
					} 
				else
					{
						out.print("No Student<br><br>");
					}
			}
	    	}catch(Exception e)
	    		{	    
	    			out.print("<script>alert('Unable To Add Attendance');</script>");
	    			System.out.println("Exception by Add Attendance Page "+e);
	    			RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAdd");
	    			rd.include(request, response);
				}
        	}catch (ParseException e) 
        		{
        			out.print("<script>alert('Enter Valid Date');</script>");
        			System.out.println("Exception by Add Attendance Page"+e);
        			RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAdd");
        			rd.include(request, response);
        		}
        out.print("<a href='userLogin.jsp'>Home</a>");
        out.println("</body></head></html>");
	}
}
