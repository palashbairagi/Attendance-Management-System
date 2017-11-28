package user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

public class UpdateUser extends HttpServlet {
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
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String userName=request.getParameter("userName");
		
		out.println("<html><head><title>View User</title></head><body>");
		
		try
			{				
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement pstmt=con.prepareStatement("select user_name from user_info where user_name=? and user_id!=?");
				pstmt.setString(1, userName);
				pstmt.setString(2, id);
				ResultSet r=pstmt.executeQuery();
		
				if(r.next())
					{
						out.print("User Name Already Exists");
						RequestDispatcher rd=request.getRequestDispatcher("EditUser");
						rd.include(request, response);
					}
				else
					{
						PreparedStatement stmt=con.prepareStatement("update user_info set first_Name=?,last_Name=?, user_Name=? where user_id=?");
						stmt.setString(1,firstName);
						stmt.setString(2,lastName);
						stmt.setString(3,userName);
						stmt.setString(4,id);			
						stmt.executeUpdate();
				
						out.println("User Updated <br><br>");
				
						RequestDispatcher rd=request.getRequestDispatcher("ViewUsers");
						rd.include(request, response);
					}
			}catch(Exception e)				
				{			
					out.print("<script>alert('Unable To Update User');</script>");
					System.out.println("Exception by UpdateUser "+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewUsers");
					rd.include(request, response);							
				}
		
		out.println("</body></html>");
					
	}

}
