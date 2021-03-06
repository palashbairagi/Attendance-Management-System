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

public class DeleteStudent extends HttpServlet {
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
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("delete from student_info where student_id=?");
				stmt.setString(1, id);
				stmt.executeUpdate();
				
				out.println("Student Deleted<br><br>");
			
				RequestDispatcher rd=request.getRequestDispatcher("student.jsp");
				rd.include(request, response);
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Delete Student');</script>");
					System.out.println("Exception by Delete Student"+e);
					RequestDispatcher rd=request.getRequestDispatcher("student.jsp");
					rd.forward(request, response);
				}
		}
}