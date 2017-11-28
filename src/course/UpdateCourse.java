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

public class UpdateCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();

			boolean isValid=other.Getter.isSessionValid(request);
			if(!isValid)
				{
					RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
					rd.forward(request, response);							
				}

			String id=request.getParameter("id");
			String branch=request.getParameter("branch");
			String section=request.getParameter("section");
			String semester=request.getParameter("semester");
			
			out.println("<html><head><title>Update Student</title></head>");
			
			try{
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
					PreparedStatement stmt1=con.prepareStatement("select branch from course_info where branch=? and semester=? and section=? and course_id!=?");
					stmt1.setString(1, branch);
					stmt1.setString(2, semester);
					stmt1.setString(3, section);
					stmt1.setString(4,id);
					ResultSet rset=stmt1.executeQuery();
			
					if(rset.next())
						{
							out.println("Course already exists<br><br>");
				    
							RequestDispatcher rd=request.getRequestDispatcher("EditCourse");
							rd.include(request, response);
						}
					else
						{
							PreparedStatement stmt=con.prepareStatement("update course_info set branch=?, semester=?, section=? where course_id=?");
							stmt.setString(1, branch);
							stmt.setString(2, semester);
							stmt.setString(3, section);
							stmt.setString(4, id);
							stmt.executeUpdate();
				
							out.println("<body>Branch Updated<br><br></body>");
					
							RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
							rd.include(request, response);
						}
				}catch(Exception e)
					{
						out.print("<script>alert('Unable To Update Course');</script>");
						System.out.println("Exception by Update Course "+e);
						RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
						rd.forward(request, response);
					}
			out.println("</html>");
		}
}
