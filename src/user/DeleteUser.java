package user;

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

public class DeleteUser extends HttpServlet {
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

		out.println("<html><head><title>View User</title></head><body>");
		
		try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("delete from user_info where user_id=?");
				stmt.setString(1, id);
				stmt.executeUpdate();
				
				out.println("User Deleted<br><br>");
				
				RequestDispatcher rd=request.getRequestDispatcher("ViewUsers");
				rd.include(request, response);
			
			}catch(Exception e)
			{
				out.print("<script>alert('Unable To Delete User');</script>");
				System.out.println("Exception by Login "+e);
				RequestDispatcher rd=request.getRequestDispatcher("ViewUsers");
				rd.include(request, response);							
			}
		
		out.println("</body></html>");
	
	}	
}