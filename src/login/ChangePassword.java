package login;

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
import javax.servlet.http.HttpSession;

public class ChangePassword extends HttpServlet {
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
		
		HttpSession session=request.getSession();
		String userId=(String) session.getAttribute("userId");		
		String currentPassword=request.getParameter("currentPassword");
		String newPassword=request.getParameter("newPassword");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
				PreparedStatement stmt=con.prepareStatement("select * from user_info where user_id=? and password=?");
				stmt.setString(1,userId);
				stmt.setString(2,currentPassword);
				ResultSet r=stmt.executeQuery();

				if(r.next())
					{
						PreparedStatement stmt1=con.prepareStatement("update user_info set password=? where user_id=?");
						stmt1.setString(1,newPassword);
						stmt1.setString(2,userId);
						stmt1.executeUpdate();

						if(r.getString(4).equals("Admin"))
							{
								out.print("<script>alert('Password Changed Successfully');</script>");
								RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
								rd.include(request, response);
							}
						else
							{
								out.print("<script>alert('Password Changed Successfully');</script>");
								RequestDispatcher rd=request.getRequestDispatcher("userLogin.jsp");
								rd.include(request, response);
							}
					}
				else
					{
						out.println("Invalid current Password");
						RequestDispatcher rd=request.getRequestDispatcher("changePassword.jsp");
						rd.include(request, response);
					}
			}catch(Exception e)
				{   
					out.print("<script>alert('Unable To Change Password');</script>");
					System.out.println("Exception by Change Password "+e);
				}
	}
}
