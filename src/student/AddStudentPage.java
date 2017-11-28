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

public class AddStudentPage extends HttpServlet {
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
		
		String courseId=request.getParameter("courseId");
		
		out.println("<html><head><title>View Student</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("select * from course_info order by semester, branch, section");
				ResultSet rset=stmt.executeQuery();				
			
				if(rset.next())
					{
						out.println("<form action='AddStudent'><table>");
						out.println("<tr><td>Course<td><select name='courseId' >");
						
						if(courseId.equals(rset.getString(1)))out.println("<option value="+rset.getString(1)+" selected='selected'>"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem </option>");
						else out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem</option>");
						
						while(rset.next())
							{
								if(courseId.equals(rset.getString(1)))out.println("<option value="+rset.getString(1)+" selected='selected'>"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem </option>");
								else out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem</option>");
							}
						
						out.print("</select>");
						out.print("<tr><td>Enrollment Number <td><input type=text name='rollNumber0'/>");out.print("<td>Name <td><input type=text name='name0'/></tr>");
						out.print("<tr><td>Enrollment Number <td><input type=text name='rollNumber1'/>");out.print("<td>Name <td><input type=text name='name1'/></tr>");
						out.print("<tr><td>Enrollment Number <td><input type=text name='rollNumber2'/>");out.print("<td>Name <td><input type=text name='name2'/></tr>");
						out.print("<tr><td>Enrollment Number <td><input type=text name='rollNumber3'/>");out.print("<td>Name <td><input type=text name='name3'/></tr>");
						out.print("<tr><td>Enrollment Number <td><input type=text name='rollNumber4'/>");out.print("<td>Name <td><input type=text name='name4'/></tr>");
						out.print("<tr><td colspan=2 align=center><input type='submit' value='Add'/></td></tr>");
						out.print("</table></form>");
					}
				else
					{
						out.println("Please Add Course<br>");
					}
			}catch(Exception e)
			{
				out.print("<script>alert('Unable To Add Student');</script>");
				System.out.println("Exception by Add Student Page "+e);
				RequestDispatcher rd=request.getRequestDispatcher("student.jsp");
				rd.forward(request, response);
			}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></html>");
	}
}
