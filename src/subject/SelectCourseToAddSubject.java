package subject;

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

public class SelectCourseToAddSubject extends HttpServlet {
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
		
		out.println("<html><head><title>Select Course</title>");
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
						out.println("<form action='AddSubjectPage'><table>");
						out.println("<tr><td>Course</td><td> <select name='courseId' >");
						out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem </option>");
			
						while(rset.next())
							{
								out.println("<option value="+rset.getString(1)+">"+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+"  sem</option>");	
							}
						out.println("</select></td>");
						out.println("<td><input type='radio' name='type' value='T' checked>Theory</td>");
						out.println("<td><input type='radio' name='type' value='P'>Practical</td></tr>");
						out.print("<tr><td colspan='4' align='center'><input type='submit' value='Go'></td></tr></table>");
					}
				else
					{
						out.println("Please Add Course<br><br>");
					}
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Select Course');</script>");
					System.out.println("Exception by Select Course To Add Subject"+e);
					RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
					rd.forward(request, response);
				}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></head></html>");
		}
}
