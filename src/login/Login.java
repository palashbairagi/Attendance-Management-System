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

public class Login extends HttpServlet {
private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			PreparedStatement stmt=con.prepareStatement("select * from user_info where user_Name=? and password=?");

			stmt.setString(1,userName);
			stmt.setString(2,password);
			ResultSet r=stmt.executeQuery();

			if(r.next())
				{
					HttpSession session=request.getSession();
					session.setAttribute("userId",r.getString(1));
					session.setAttribute("userName",r.getString(4));
					session.setAttribute("password",r.getString(5));

					if(userName.equals("Admin")||userName.equals("admin"))
						{
							RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
							rd.include(request, response);
						}
					else
						{
							RequestDispatcher rd=request.getRequestDispatcher("userLogin.jsp");
							rd.include(request, response);
						}
				}
			else
				{
					out.println("Invalid User Name or Password");
					RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
					rd.include(request, response);
				}
			}
			catch(Exception e)
			{
				out.print("<script>alert('Unable To Login');</script>");
				System.out.println("Exception by Login "+e);
				RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
				rd.include(request, response);
			}
		}
}
