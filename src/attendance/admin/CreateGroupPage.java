package attendance.admin;

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

public class CreateGroupPage extends HttpServlet {
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
		
		String courseId=request.getParameter("id");
		
		out.println("<html><head>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		out.print("<form action='CreateGroup'>");
		out.print("<table><tr><th>Enrollment<th>Name</tr>");
		try{	
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
				PreparedStatement pstmt=con.prepareStatement("select * from student_info where course_id=? and group_name is null order by roll_number, name");
				pstmt.setString(1, courseId);
				ResultSet rset=pstmt.executeQuery();
				
				int i=0;
				out.print("Group Name<input type='radio' name='groupName' value='A' checked>Group A");
				out.print("<input type='radio' name='groupName' value='B' >Group B");
				while(rset.next())
					{	
						out.print("<input type='hidden' name='studentId"+i+"' value='"+rset.getString(1)+"'>");
						out.print("<tr><td>"+rset.getString(2)+"<td>"+rset.getString(3));
						out.print("<td><input type='checkbox' name='check"+i+"'></tr>");
						i++;
					}
				out.print("<input type='hidden' name='variable' value='"+i+"'>");
				out.print("<tr><input type='submit' value='OK'>");
			}catch(Exception e)
				{
					out.print("<script>alert('Unable Create Group');</script>");
					System.out.println("Exception by Create Group Page  "+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
					rd.forward(request, response);
				}
		out.print("</table></form>");
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.print("</body></html>");
	}
}
