package student;

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

public class AddStudent extends HttpServlet {
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
		
		String rollNumber[]=new String[5];
		rollNumber[0]=request.getParameter("rollNumber0");
		rollNumber[1]=request.getParameter("rollNumber1");
		rollNumber[2]=request.getParameter("rollNumber2");
		rollNumber[3]=request.getParameter("rollNumber3");
		rollNumber[4]=request.getParameter("rollNumber4");
		
		String name[]=new String[5];
		name[0]=request.getParameter("name0");
		name[1]=request.getParameter("name1");
		name[2]=request.getParameter("name2");
		name[3]=request.getParameter("name3");
		name[4]=request.getParameter("name4");
		
		String courseId=request.getParameter("courseId");

		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				int e=0;			
				while(e<=4)
					{
						if(name[e].length()!=0 || rollNumber[e].length()!=0)
							{
								PreparedStatement stmt=con.prepareStatement("insert into student_info (roll_number,name,course_id) values (?,?,?)");
								stmt.setString(1, rollNumber[e]);
								stmt.setString(2,name[e]);
								stmt.setString(3,courseId);
								stmt.executeUpdate();
							}
						e++; 				
					}
				 
				out.print("Student Added");
				 
				RequestDispatcher rd=request.getRequestDispatcher("AddStudentPage?courseId="+courseId);
				rd.include(request, response);
			
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Add Student');</script>");
					System.out.println("Exception by Add Student "+e);
					RequestDispatcher rd=request.getRequestDispatcher("student.jsp");
					rd.forward(request, response);
				}
	}

}
