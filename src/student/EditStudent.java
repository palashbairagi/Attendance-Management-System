package student;

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

public class EditStudent extends HttpServlet {
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
		
		out.println("<html><head><title>Edit Student</title>");
		out.print("<script src='jquery-1.9.1.js'></script>");
		out.print("<script src='jquery.validate.min.js'></script>");
		out.print("<script src='bootstrap.min.js'></script>");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("</head><body>");
		
		String courseId=request.getParameter("courseId");
		String id=request.getParameter("id");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("select * from student_info where student_id=?");
				stmt.setString(1, id);
				ResultSet r=stmt.executeQuery();
			
				if(r.next())
					{	
						out.println("<form action='UpdateStudent' name='editStudent' id='editStudent'>" +
								"<input type='hidden' name='courseId' value="+courseId+"><input type='hidden' name='id' value='"+r.getString(1)+"'>");
						out.print("<table><tr><td>Enrollment Number</td><td><input type=text name='rollNumber' id='rollNumber' value='"+r.getString(2)+"' /></td></tr>");
						out.print("<tr><td>Name</td><td><input type='text' name='name' id='name' value='"+r.getString(3)+"' /></td></tr>" +
								"<tr><td colspan='2' align='center'><input type='submit' value='Update'></td></tr></table></form>");
					}
				else
					{
						out.println("Invalid Selection<br><br>");
					}
			}
			catch(Exception e)
				{
					out.print("<script>alert('Unable To Edit Student');</script>");
					System.out.println("Exception by Edit Student"+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewStudents");
					rd.forward(request, response);
				}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></html>");
	}
}

	
	