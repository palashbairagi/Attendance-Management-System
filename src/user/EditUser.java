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

public class EditUser extends HttpServlet {
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

		out.println("<html><head><title>Edit User</title></head>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script src='jquery-1.9.1.js'></script>");
		out.print("<script src='jquery.validate.min.js'></script>");
		out.print("<script src='bootstrap.min.js'></script>");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		try
			{			
				Class.forName("com.mysql.jdbc.Driver");		
				Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");

				PreparedStatement stmt=con.prepareStatement("select * from user_info where user_id=?");
				stmt.setString(1,id);
				ResultSet r=stmt.executeQuery();

				if(r.next())
					{	
						out.println("<form action='UpdateUser' id='userRegistration'><table><input type='hidden' name='id' value='"+r.getString(1)+"'>");
						out.println("<tr><td>First Name</td><td><input type='text' name='firstName' id='firstName' value='"+r.getString(2)+"'></td></tr>");				
						out.println("<tr><td>Last Name</td><td><input type='text' name='lastName' id='lastName' value='"+r.getString(3)+"'></td></tr>");
						out.println("<tr><td>User Name</td><td><input type='text' name='userName' id='userName' value='"+r.getString(4)+"' disable='disabled'></td></tr>");
						out.println("<tr><td colspan='2' align='center'><input type='submit' value='Update' /></td></tr>");			
					}
				else
					{
						out.println("Invalid Selection");
					}	  
			}
			catch(Exception e)
			{
				out.print("<script>alert('Unable To Edit User');</script>");
				System.out.println("Exception by EditUser "+e);
				RequestDispatcher rd=request.getRequestDispatcher("ViewUsers");
				rd.include(request, response);							
			}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></html>");
	}
}
