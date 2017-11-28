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

public class ViewStudents extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    	{    		
    		response.setContentType("text/html");
    		PrintWriter out=response.getWriter();
 

    		boolean isValid=other.Getter.isSessionValid(request);
    		if(!isValid)
    			{
    				RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");
    				rd.forward(request, response);							
    			}
    		
    		out.println("<html><head><title>View Student</title>");
    		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
    		out.print("<script type='text/javascript' src='validation.js'></script>");
    		out.print("</head><body>");
    		out.print("<a href='student.jsp'>Home</a>");
    		
    		String courseId=request.getParameter("courseId");
    		
    		try{		
    				Class.forName("com.mysql.jdbc.Driver");
    				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
    		
    				PreparedStatement pstmt=con.prepareStatement("select * from course_info where course_id=?");
    				pstmt.setString(1, courseId);
    				ResultSet rst=pstmt.executeQuery();
    				
    				if(rst.next())
    					{
    						out.print("<table><tr><td>Course</td><td>"+rst.getString(2)+" "+rst.getString(3)+" "+rst.getString(4)+" sem ");
    						out.print("</td></tr></table>");
    					}
    				
    				PreparedStatement stmt=con.prepareStatement("select * from student_info where course_id=? order by roll_number, name");
    				stmt.setString(1, courseId);
    				ResultSet r=stmt.executeQuery();
    		
    				if(r.next())
    					{
    						out.print("<table><tr>");
    						out.print("<td><input type='hidden' value='"+r.getString(1)+"' name='id'>");
    						out.print("<td>"+r.getString(2)+"<td>"+r.getString(3));
    						out.print("<td><a href='EditStudent?id="+r.getString(1)+"&courseId="+courseId+"'>Edit</a>");
							out.print("<td><a href='DeleteStudent?id="+r.getString(1)+"&courseId="+courseId+"'>Delete</a></tr>");
						
    						while(r.next())
    							{
    								out.print("<td><input type='hidden' value='"+r.getString(1)+"'>");
    								out.print("<td>"+r.getString(2)+"<td>"+r.getString(3));
    								out.print("<td><a href='EditStudent?id="+r.getString(1)+"&courseId="+courseId+"'>Edit</a>");
    								out.print("<td><a href='DeleteStudent?id="+r.getString(1)+"&courseId="+courseId+"'>Delete</a></tr>");
    							}
    						out.print("</form></table>");
    					} 
    				else
    					{
    						out.print("No Student<br><br>");
    					}
    		}catch(Exception e)
    			{	    
    				out.print("<script>alert('Unable To View Students');</script>");
    				System.out.println("Exception by View Students"+e);
    				RequestDispatcher rd=request.getRequestDispatcher("adminLogin.jsp");
    				rd.forward(request, response);
    			}
    		out.print("<a href='adminLogin.jsp'>Home</a>");
    		out.println("</body></html>");
   	}
}   