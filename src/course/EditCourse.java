package course;

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

public class EditCourse extends HttpServlet {
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
			
			String id=request.getParameter("id");
			out.println("<html><head><title>Edit Course</title>");
			out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
			out.print("<script type='text/javascript' src='validation.js'></script>");
			out.print("</head><body>");
			
			try{
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
					PreparedStatement stmt=con.prepareStatement("select * from course_info where course_id=?");
					stmt.setString(1, id);
					ResultSet r=stmt.executeQuery();
				
					if(r.next())
						{	
							out.print("<form action='UpdateCourse'><input type='hidden' name='id' value='"+r.getString(1)+"'>");
							out.print("<table><tr><td>Branch</td><td><select name='branch'>");
						
							if(r.getString(2).equals("CS"))
								{
									out.print("<option value='CS' Selected='selected'>CS</option>");
									out.print("<option value='IT'>IT</option>");
									out.print("<option value='EC'>EC</option>");
									out.print("<option value='CE'>CE</option>");
									out.print("<option value='ME'>ME</option>");
								}
							if(r.getString(2).equals("IT"))
							{
								out.print("<option value='CS'>CS</option>");
								out.print("<option value='IT' Selected='selected'>IT</option>");
								out.print("<option value='EC'>EC</option>");
								out.print("<option value='CE'>CE</option>");
								out.print("<option value='ME'>ME</option>");
							}
							if(r.getString(2).equals("EC"))
							{
								out.print("<option value='CS'>CS</option>");
								out.print("<option value='IT'>IT</option>");
								out.print("<option value='EC' Selected='selected'>EC</option>");
								out.print("<option value='CE'>CE</option>");
								out.print("<option value='ME'>ME</option>");
							}
							if(r.getString(2).equals("CE"))
							{
								out.print("<option value='CS'>CS</option>");
								out.print("<option value='IT'>IT</option>");
								out.print("<option value='EC'>EC</option>");
								out.print("<option value='CE' Selected='selected'>CE</option>");
								out.print("<option value='ME'>ME</option>");
							}
							if(r.getString(2).equals("ME"))
							{
								out.print("<option value='CS'>CS</option>");
								out.print("<option value='IT'>IT</option>");
								out.print("<option value='EC'>EC</option>");
								out.print("<option value='CE'>CE</option>");
								out.print("<option value='ME' Selected='selected'>ME</option>");
							}
						out.print("</select></td></tr>");
							
							out.print("<tr><td>Semester");
							out.print("</td><td><select name='semester'>"); 
							if(r.getString(4).equals("1st"))out.print("<option value='1st' Selected='selected'>1st</option>");else out.print("<option value='1st'>1st</option>");
							if(r.getString(4).equals("2nd"))out.print("<option value='2nd' Selected='selected'>2nd</option>");else out.print("<option value='2nd'>2nd</option>");
							if(r.getString(4).equals("3rd"))out.print("<option value='3rd' Selected='selected'>3rd</option>");else out.print("<option value='3rd'>3rd</option>");
							if(r.getString(4).equals("4th"))out.print("<option value='4th' Selected='selected'>4th</option>");else out.print("<option value='4th'>4th</option>");
							if(r.getString(4).equals("5th"))out.print("<option value='5th' Selected='selected'>5th</option>");else out.print("<option value='5th'>5th</option>");
							if(r.getString(4).equals("6th"))out.print("<option value='6th' Selected='selected'>6th</option>");else out.print("<option value='6th'>6th</option>");
							if(r.getString(4).equals("7th"))out.print("<option value='7th' Selected='selected'>7th</option>");else out.print("<option value='7th'>7th</option>");
							if(r.getString(4).equals("8th"))out.print("<option value='8th' Selected='selected'>8th</option>");else out.print("<option value='8th'>8th</option>");
							out.print("</select></td></tr>");
							
							out.print("<tr><td>Section");
							out.print("</td><td><input type='text' name='section' value='"+r.getString(3)+"'></td></tr>");
							
							out.print("<tr><td colspan='2' align='center'><input type='submit' value='Update'></td></tr></table></form>");
						}
					else
						{
							out.println("Invalid Selection<br><br>");
						}
			    	  
				}catch(Exception e)
					{
						out.print("<script>alert('Unable To Edit Course');</script>");
						System.out.println("Exception by Edit Course "+e);
						RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
						rd.forward(request, response);
					}
			out.print("<a href='adminLogin.jsp'>Home</a>");
			out.println("</body></html>");  
		}
	}

		
		
	