package attendance.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateGroup extends HttpServlet {
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
			
			String groupName=request.getParameter("groupName");

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
		
						int variable=Integer.parseInt(request.getParameter("variable"));
						String studentId=null;
						String checkBox=null;
						PreparedStatement stmt;
						int i=0;
						while(i<variable)
							{
								studentId=request.getParameter("studentId"+i);
								checkBox=request.getParameter("check"+i);

								if(checkBox!=null)if(checkBox.equals("on"))
									{
										stmt=con.prepareStatement("update student_info set group_name=? where student_id=?");
										stmt.setString(1, groupName);
										stmt.setString(2, studentId);	
										stmt.executeUpdate();
									}
								i++;
							}
						
						out.println("Group Added");
						RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
						rd.include(request, response);
					
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Create Group');</script>");
					System.out.println("Exception by Create Group "+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewCourses");
					rd.forward(request, response);
				}
	}
}
