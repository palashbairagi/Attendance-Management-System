package subject;

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

public class EditSubject extends HttpServlet {
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
			
			out.println("<html><head><title>Edit Subject</title></head>");
			out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
			out.print("<script type='text/javascript' src='validation.js'></script>");
			out.print("</head><body>");
			
			String id=request.getParameter("id");
			String subjectName=request.getParameter("subjectName");
			String type=request.getParameter("type");
			String branch=request.getParameter("branch");
			String section=request.getParameter("section");
			String semester=request.getParameter("semester");
			
			if(type.equals("T"))type="Theory";
			else type="Practical";
			
			try{
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
					PreparedStatement stmt=con.prepareStatement("select * from subject_info where subject_id=?");
					stmt.setString(1, id);
					ResultSet r=stmt.executeQuery();
				
					if(r.next())
						{	
							String userId=r.getString(5);
							
							out.println("<form action='UpdateSubject'><input type='hidden' name='id' value='"+r.getString(1)+"'>");
							
							PreparedStatement pstmt=con.prepareStatement("select * from user_info where user_name!='Admin' order by first_Name,last_Name");
							ResultSet rs=pstmt.executeQuery();
					
								if(rs.next())
									{
									out.println("<table><tr><td>Subject</td><td>"+subjectName+"</td></tr>" +
									"<tr><td>Type</td><td>"+type+"</td></tr>"+
									"<tr><td>Course</td><td>"+branch+" "+section+" "+semester+"</td></tr>"+
									"<tr><td>Faculty</td><td> <select name='userId' >");
									
									if(userId.equals(rs.getString(1)))out.println("<option value="+rs.getString(1)+" selected='selected'>"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");
									else out.println("<option value="+rs.getString(1)+" >"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");
									
									while(rs.next())
										{
											if(userId.equals(rs.getString(1)))out.println("<option value="+rs.getString(1)+" selected='selected'>"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");
											else out.println("<option value="+rs.getString(1)+" >"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");
										
										}
					
									out.println("</select></td></tr>");
									}
					
							out.print("<tr><td colspan='2' align='center'><input type='submit' value='Update'></td></tr></table></form>");
						}
					else
						{
							out.println("Invalid Selection");
						}
				}
				catch(Exception e)
					{
						out.print("<script>alert('Unable To Edit Subject');</script>");
						System.out.println("Exception by Edit Subject"+e);
						RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
						rd.forward(request, response);
					}
			out.print("<a href='adminLogin.jsp'>Home</a>");
			out.println("</body></html>");
		}
	}
