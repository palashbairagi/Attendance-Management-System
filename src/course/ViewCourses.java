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

public class ViewCourses extends HttpServlet {
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
		
		out.println("<html><head>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		out.print("<a href='addCourse.jsp'>Add</a>");
		
		try{	
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				PreparedStatement stmt=con.prepareStatement("select * from course_info order by semester, branch, section");
				ResultSet r=stmt.executeQuery();
		
				if(r.next())
					{
						out.print("<table><tr><th>Branch<th>Section<th>Semester<th>Edit/Delete<th>Group<tr>");
							
						out.print("<td>"+r.getString(2));
						out.print("<td>"+r.getString(3));
						out.print("<td>"+r.getString(4));
						out.print("<td><a href='EditCourse?id="+r.getString(1)+"'>Edit</a>");
						out.print("&nbsp;&nbsp;&nbsp<a href='DeleteCourse?id="+r.getString(1)+"'>Delete</a>");
						out.print("<td><a href='CreateGroupPage?id="+r.getString(1)+"'>Create Group</a></tr>");
						
						while(r.next())
							{
								out.print("<td>"+r.getString(2));
								out.print("<td>"+r.getString(3));
								out.print("<td>"+r.getString(4));
								out.print("<td><a href='EditCourse?id="+r.getString(1)+"'>Edit</a>");
								out.print("&nbsp;&nbsp;&nbsp<a href='DeleteCourse?id="+r.getString(1)+"'>Delete</a>");
								out.print("<td><a href='CreateGroupPage?id="+r.getString(1)+"'>Create Group</a></tr>");
							}
						out.print("</form></table>");
					}
				else
					{
						out.print("No Course");
					}
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To View Course');</script>");
					System.out.println("Exception by View Course "+e);
					RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
					rd.forward(request, response);
				}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></head></html>");
	}

}
