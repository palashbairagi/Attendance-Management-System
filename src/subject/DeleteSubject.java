package subject;

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

public class DeleteSubject extends HttpServlet {
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
		
		String id=request.getParameter("id");
		
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
			
				PreparedStatement stmt=con.prepareStatement("delete from subject_info where subject_id=?");
				stmt.setString(1, id);
				stmt.executeUpdate();
				
				out.println("Subject Deleted <br><br>");
				RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
				rd.include(request, response);
		
			}catch(Exception e)
				{
					out.print("<script>alert('Unable To Delete Subject');</script>");
					System.out.println("Exception by Delete Subject"+e);
					RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
					rd.forward(request, response);
				}
	}
}