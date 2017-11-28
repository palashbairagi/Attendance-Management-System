package student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateStudent extends HttpServlet {
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
		
		out.println("<html><head><title>Update Student</title></head><body>");
		
		String courseId=request.getParameter("courseId");
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		String rollNumber=request.getParameter("rollNumber");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("update student_info set roll_number=?, name=? where student_id=?");
				stmt.setString(1,rollNumber);
				stmt.setString(2,name);
				stmt.setString(3,id);
				stmt.executeUpdate();
			
				out.println("Student Updated<br><br>");
				
				RequestDispatcher rd=request.getRequestDispatcher("ViewStudents?courseId="+courseId);
				rd.include(request, response);
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Update');</script>");
					System.out.println("Exception by Update Student "+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewStudents");
					rd.forward(request, response);
				}
			out.println("</body></html>");
		}
	}