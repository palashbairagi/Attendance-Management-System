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


public class AddAttendance extends HttpServlet {
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
		String courseId=request.getParameter("courseId");
		String date=request.getParameter("date");
	    String time=request.getParameter("time");
	    String userId=request.getParameter("userId");
	    String groupName=request.getParameter("groupName");
	    String type=request.getParameter("type");
	    int variable=Integer.parseInt(request.getParameter("variable"));
	    
	    String attendance;
	    String studentId;
        
	try{	
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
			if(type.equals("T"))
				{
					PreparedStatement ps=con.prepareStatement("select attendance from attendance_info where date=? and time=?");
					ps.setString(1,date);
					ps.setString(2,time);
					ResultSet rst=ps.executeQuery();
				
					if(rst.next())
					{
						out.print("Attendance already inserted for "+date+"  "+time);
						RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAddAttendance");
						rd.include(request, response);
						return;
					}
				}
			
			else if(type.equals("P"))
				{
					PreparedStatement ps=con.prepareStatement("select subject_id from view_attendance_by_subject where date=? and time=? group by subject_id ");
					ps.setString(1,date);
					ps.setString(2,time);
					ResultSet rst=ps.executeQuery();
				
					if(rst.next())
						{
							PreparedStatement ps1=con.prepareStatement("select subject_id from view_attendance_by_subject where date=? and time=? and group_name=? group by subject_id ");
							ps1.setString(1,date);
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
				}
			
			for(int i=0;i<variable;i++)
				{
					attendance=request.getParameter("attendance"+i);
					studentId=request.getParameter("studentId"+i);
			
					PreparedStatement stmt=con.prepareStatement("insert into attendance_info (student_id,subject_id,date,time,attendance,user_id,course_id) values(?,?,?,?,?,?,?)");
					stmt.setString(1, studentId);
					stmt.setString(2, subjectId);
					stmt.setString(3, date);
					stmt.setString(4, time);
					stmt.setString(5, attendance);
					stmt.setString(6, userId);
					stmt.setString(7, courseId);
					stmt.executeUpdate();
				}
			out.print("Attendance Added<br><br>");
			
			RequestDispatcher rd=request.getRequestDispatcher("userLogin.jsp");
			rd.include(request, response);
		}catch(Exception e)
			{	
				out.print("<script>alert('Unable To Add Attendance');</script>");
				System.out.println("Exception by Add Attendance"+e);
				RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectToAddAttendance");
				rd.include(request, response);
			} 
	}
}
 
