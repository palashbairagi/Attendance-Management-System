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

public class AddSubject extends HttpServlet {
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
		
		String subjectName=request.getParameter("subjectName");
		String courseId=request.getParameter("courseId");
		String userId=request.getParameter("userId");
		String type=request.getParameter("type");
		
		try {
				Class.forName("com.mysql.jdbc.Driver");		
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
		
				PreparedStatement pstmt=con.prepareStatement("select * from subject_info where course_id=? and name=? and type=?");
				pstmt.setString(1, courseId);
				pstmt.setString(2, subjectName);
				pstmt.setString(3, type);
				ResultSet r=pstmt.executeQuery();
			
				if(r.next())
					{
						out.print("Already Exists<br><br>");
						RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
						rd.include(request, response);
					}
				else
					{
						PreparedStatement stmt=con.prepareStatement("insert into subject_info (name,type,course_id,user_id) values(?,?,?,?)");
						stmt.setString(1, subjectName);
						stmt.setString(2, type);
						stmt.setString(3, courseId);
						stmt.setString(4, userId);
						stmt.executeUpdate();
			
						out.println("Subject added<br><br>");
						RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
						rd.include(request, response);
					}
			}catch (Exception e) 
				{
					out.print("<script>alert('Unable To Add Subject');</script>");
					System.out.println("Exception by Add Subject "+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
					rd.forward(request, response);
				}
	}
}
 