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

public class AddCourse extends HttpServlet {
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
		
		String branch=request.getParameter("branch");
		String semester=request.getParameter("semester");
		String section=request.getParameter("section");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				PreparedStatement stmt=con.prepareStatement("select branch from course_info where branch=? and semester=? and section=?");
				stmt.setString(1, branch);
				stmt.setString(2, semester);
				stmt.setString(3, section);
				ResultSet rset=stmt.executeQuery();
		
				if(rset.next())
					{
						out.println("Course already exists<br><br>");
			    
						RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
						rd.include(request, response);
					}
				else
					{
						stmt=con.prepareStatement("insert into course_info (branch,section,semester) values (?,?,?)");
						stmt.setString(1, branch);
						stmt.setString(2, section);
						stmt.setString(3, semester);
						stmt.executeUpdate();
						
						out.println("Course Added<br><br>");
						RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
						rd.include(request, response);
					}
		
			}catch(Exception e)
				{
				out.print("<script>alert('Unable To Add Course');</script>");
				System.out.println("Exception by Add Course "+e);
				RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
				rd.forward(request, response);
				}
		}

	}


