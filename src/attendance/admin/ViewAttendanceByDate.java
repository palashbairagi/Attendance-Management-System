package attendance.admin;

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

import other.Getter;

public class ViewAttendanceByDate extends HttpServlet {
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
			
			String courseId=request.getParameter("courseId");
			String date=request.getParameter("date");
			String click=request.getParameter("click");
			
			String time=null;
			String studentId=null;
			String studentName=null;
			String rollNumber=null;
			String subjectId=null;
			String sqlDate=null;
			String attendance="";
			String color="";
			boolean printStudentName=true;
			boolean recordExists=false;
			
			DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");  
	        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd");  
	        
	    try 
	        {	

				if(click.equals("next"))date=Getter.getNextDate(date);
				else if(click.equals("previous"))date=Getter.getPreviousDate(date);
			
	        	java.util.Date utilDateTo = userDateFormat.parse(date);
	        	sqlDate = dateFormatNeeded.format(utilDateTo);
	        	
				out.println("<html><head><title>View Attendance</title><head>");
				out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
				out.print("</head><Body>");
				
			try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
				PreparedStatement pstmt=con.prepareStatement("SELECT subject_id,time FROM view_attendance_by_subject where course_id=? and date=? and time>'05:00:00' group by time");
				pstmt.setString(1, courseId);
				pstmt.setString(2, sqlDate);
				ResultSet prset=pstmt.executeQuery();				
				
				out.print("<table><th>"+date+"</th></table>");
				if (prset.next())
					{
						recordExists=true;
						
						out.print("<table>");
						out.print("<tr><th>Enrollment No<th>Name");
						
						subjectId=prset.getString(1);
						
						PreparedStatement ps=con.prepareStatement("Select name,type from subject_info where subject_id=?");
						ps.setString(1, subjectId);
						ResultSet r=ps.executeQuery();
						
						if(r.next())out.print("<th>"+Getter.getShortName(r.getString(1))+"("+r.getString(2)+")<br>");
						out.print(Getter.getShortTime(prset.getString(2))+"</th>");
					}
				
				while(prset.next() && recordExists)
					{	
						subjectId=prset.getString(1);
						
						PreparedStatement ps=con.prepareStatement("Select name,type from subject_info where subject_id=?");
						ps.setString(1, subjectId);
						ResultSet r=ps.executeQuery();
						
						if(r.next())out.print("<th>"+Getter.getShortName(r.getString(1))+"("+r.getString(2)+")<br>");
						out.print(Getter.getShortTime(prset.getString(2))+"</th>");
					}
				
				PreparedStatement pstmt1=con.prepareStatement("SELECT subject_id,time FROM view_attendance_by_subject where course_id=? and date=? and time<'05:00:00' group by time");
				pstmt1.setString(1, courseId);
				pstmt1.setString(2, sqlDate);
				ResultSet prset1=pstmt1.executeQuery();				

				while(prset1.next())
					{	
						subjectId=prset1.getString(1);
						
						PreparedStatement ps=con.prepareStatement("Select name,type from subject_info where subject_id=?");
						ps.setString(1, subjectId);
						ResultSet r=ps.executeQuery();
						
						if(r.next())out.print("<th>"+Getter.getShortName(r.getString(1))+"("+r.getString(2)+")<br>");
						out.print(Getter.getShortTime(prset1.getString(2))+"</th>");
					}
				if(recordExists)out.print("<th>Total<th></tr>");
				
				PreparedStatement stmt=con.prepareStatement("SELECT student_id,roll_number,name FROM student_info where course_id=? group by name");
				stmt.setString(1, courseId);
				ResultSet rset=stmt.executeQuery();				

				while(rset.next() && recordExists)
				
				{
						studentId=rset.getString(1);
						rollNumber=rset.getString(2);
						studentName=rset.getString(3);
						
						PreparedStatement ps=con.prepareStatement("SELECT time FROM view_attendance_by_subject where course_id=? and date=? and time>'05:00:00' group by time");
						ps.setString(1, courseId);
						ps.setString(2, sqlDate);
						ResultSet rs=ps.executeQuery();
						
						while(rs.next())
							{
								time=rs.getString(1);
								PreparedStatement p=con.prepareStatement("SELECT attendance FROM view_attendance_by_subject where student_id=? and date=? and time=?");
								p.setString(1,studentId);
								p.setString(2,sqlDate);
								p.setString(3,time);
								ResultSet r=p.executeQuery();
					
								if(r.next())
									{	
										attendance = r.getString(1);
										if(attendance.equals("A"))color="red";
										else if(attendance.equals("P"))color="green";
										else color="yellow";
										
										if(printStudentName)
											{
											out.print("<td>"+rollNumber+"<td>"+studentName+"<td align='center' style='color:"+color+"'>"+attendance);
											printStudentName=false;
											}
										else 
											{
												out.print("<td align='center' style='color:"+color+"'>"+attendance);
											}										
									}
								else
									{	
										if(printStudentName)
											{
												out.print("<td>"+rollNumber+"<td>"+studentName+"<td></td>");
												printStudentName=false;
											}
										else 
											{
												out.print("<td></td>");
											}
									}
							}
								
								PreparedStatement ps1=con.prepareStatement("SELECT time FROM view_attendance_by_subject where course_id=? and date=? and time<'05:00:00' group by time");
								ps1.setString(1, courseId);
								ps1.setString(2, sqlDate);
								ResultSet rs1=ps1.executeQuery();
								
								while(rs1.next())
									{
										time=rs1.getString(1);
										PreparedStatement p1=con.prepareStatement("SELECT attendance FROM view_attendance_by_subject where student_id=? and date=? and time=?");
										p1.setString(1,studentId);
										p1.setString(2,sqlDate);
										p1.setString(3,time);
										ResultSet r1=p1.executeQuery();
							
										if(r1.next())
											{	
												attendance = r1.getString(1);
												if(attendance.equals("A"))color="red";
												else if(attendance.equals("P"))color="green";
												else color="yellow";
												
												if(printStudentName)
													{
													out.print("<td>"+rollNumber+"<td>"+studentName+"<td align='center' style='color:"+color+"'>"+attendance);
													printStudentName=false;
													}
												else 
													{
														out.print("<td align='center' style='color:"+color+"'>"+attendance);
													}										
											}
										else
											{	
												if(printStudentName)
													{
														out.print("<td>"+rollNumber+"<td>"+studentName+"<td></td>");
														printStudentName=false;
													}
												else 
													{
														out.print("<td></td>");
													}
											}
									}
								PreparedStatement prepared=con.prepareStatement("SELECT count(*) FROM view_attendance_by_subject where student_id=? and date=? and attendance='P'");
								prepared.setString(1,studentId);
								prepared.setString(2,sqlDate);
								ResultSet rprepared=prepared.executeQuery();
								 
								if(rprepared.next())
									{
										out.print("<td><b>"+rprepared.getString(1)+"</b>");
									}
								else
									{
										out.print("<td></td>");
									}

						out.print("</tr>");
						printStudentName=true;
					}
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To View Attendance');</script>");
					System.out.println("Exception by View Attendance By Date "+e);
				//	RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
				//	rd.forward(request, response);
				}
	        }catch (ParseException e) 
    		{
    			out.print("<script>alert('Enter Valid Date');</script>");
    			System.out.println("Exception by View Attendance By Date "+e);
    		//	RequestDispatcher rd=request.getRequestDispatcher("SelectSubjectAndDateToView");
    		//	rd.include(request, response);
    		}
	    	
	    	if(!recordExists){out.print("<table align='center'><tr><td>No Attendance</td></tr></table>");}
	    	out.print("<tr><table><tr><td><a href='ViewAttendanceByDate?courseId="+courseId+"&date="+date+"&click=previous'>Previous</a>");
	    	out.print("<td align='right'><a href='ViewAttendanceByDate?courseId="+courseId+"&date="+date+"&click=next'>Next</a></table>");
	    	out.print("</table>");
	    	out.println("</form>");
	    	out.print("<a href='adminLogin.jsp'>Home</a>");
	    	out.print("</body></html>");
	}

}
 