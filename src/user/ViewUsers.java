package user;

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

public class ViewUsers extends HttpServlet {
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
		
		out.println("<html><title>User</title><head>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		out.print("<a href='addUser.jsp'>Add</a>");
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			PreparedStatement stmt=con.prepareStatement("select * from user_info where user_name !='admin' order by first_name,last_name");
			ResultSet r=stmt.executeQuery();
		
			if(r.next())
				{
					out.print("<table><tr></tr>");
					out.print("<tr><th>Name<th>User Name<th>Edit/Delete<tr>");
					out.print("<td>"+r.getString(2)+" "+r.getString(3));
					out.print("<td>"+r.getString(4));
					out.print("<td><a href='EditUser?id="+r.getString(1)+"'>Edit</a>");
					out.print("&nbsp;&nbsp;&nbsp<a href='DeleteUser?id="+r.getString(1)+"'>Delete</a></tr>");
		
					while(r.next())	
						{
							out.print("<td>"+r.getString(2)+" "+r.getString(3));
							out.print("<td>"+r.getString(4));
							out.print("<td><a href='EditUser?id="+r.getString(1)+"'>Edit</a>");
							out.print("&nbsp;&nbsp;&nbsp<a href='DeleteUser?id="+r.getString(1)+"'>Delete</a></tr>");
						}
					out.print("</table>");
				}
			else
				{
					out.print("<br>No Users<br>");	
				}
	
		}catch(Exception e)
			{
				out.print("<script>alert('Unable To View User');</script>");
				System.out.println("Exception by ViewUser "+e);
				RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
				rd.include(request, response);							
			}
		out.print("<br><a href='adminLogin.jsp'>Home</a>");
		out.println("</body></head></html>");
	}
}
