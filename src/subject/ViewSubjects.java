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

public class ViewSubjects extends HttpServlet {
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
		
		out.print("<a href='SelectCourseToAddSubject'>Add</a><br>");
		
		try{	
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				PreparedStatement stmt=con.prepareStatement("select * from view_course_and_its_subjects order by name");
				ResultSet r=stmt.executeQuery();

				if(r.next())
					{
						out.print("<table><tr><th>Subject<th>Practical/Theory<th>Course<th>Faculty<th>Edit/Delete<tr>");
						out.print("<td>"+r.getString(2));
						out.print("<td align='center'>"+r.getString(3));
						out.print("<td width='100' align='center'>"+r.getString(4)+" "+r.getString(5)+" "+r.getString(6)+" Sem");
						out.print("<td>"+r.getString(7)+" "+r.getString(8));
						out.print("<td><a href='EditSubject?id="+r.getString(1)+"&subjectName="+r.getString(2)+"&type="+r.getString(3)+"&branch="+r.getString(4)+"&section="+r.getString(5)+"&semester="+r.getString(6)+"'>Edit</a>");
						out.print("&nbsp;&nbsp;&nbsp<a href='DeleteSubject?id="+r.getString(1)+"'>Delete</a></tr>");

						while(r.next())
							{
								out.print("<td>"+r.getString(2));
								out.print("<td align='center'>"+r.getString(3));
								out.print("<td width='100' align='center'>"+r.getString(4)+" "+r.getString(5)+" "+r.getString(6)+" Sem");
								out.print("<td>"+r.getString(7)+" "+r.getString(8));
								out.print("<td><a href='EditSubject?id="+r.getString(1)+"&subjectName="+r.getString(2)+"&type="+r.getString(3)+"&branch="+r.getString(4)+"&section="+r.getString(5)+"&semester="+r.getString(6)+"'>Edit</a>");
								out.print("&nbsp;&nbsp;&nbsp<a href='DeleteSubject?id="+r.getString(1)+"'>Delete</a></tr>");
							}	
						out.print("</table>");
					}
				else
					{
						out.print("<br>No Subject<br>");
					}
			}catch(Exception e)
				{
				out.print("<script>alert('Unable To View Subject');</script>");
				System.out.println("Exception by View Subject "+e);
				RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
				rd.forward(request, response);
				}
		out.print("<a href='adminLogin.jsp'>Home</a>");
		out.println("</body></head></html>");
	}
}