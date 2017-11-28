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

public class AddSubjectPage extends HttpServlet {
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
		
		out.println("<html><head><title>Add Subject</title>");
		out.print("<link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		out.print("<script type='text/javascript' src='validation.js'></script>");
		out.print("</head><body>");
		
		String courseId=request.getParameter("courseId");
		String type=request.getParameter("type");
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MITM","root","root");
				
				PreparedStatement pstmt=con.prepareStatement("select * from course_info where course_Id=?");
				pstmt.setString(1, courseId);
				ResultSet r=pstmt.executeQuery();
				
					if(r.next())
						{
						 	String branch=r.getString(2);
							String semester=r.getString(4);
							
							out.print("<form action='AddSubject'>");
							out.print("<input type='hidden' name='courseId' value='"+courseId+"'>");
							out.print("<input type='hidden' name='type' value='"+type+"'>");
							out.print("<table><tr><td>Subject</td><td><select name='subjectName'>");
							
							if(branch.equals("CS") && type.equals("P"))
								{
								if(semester.equals("3rd"))
									{
										out.println("<option value='CS-303 Digital Circuit & System'>CS-303 Digital Circuit & System</option>");
										out.println("<option value='CS-304 Electronics Devices & Circuits'>CS-304 Electronics Devices & Circuits</option>");
										out.println("<option value='CS-305 Data Structures'>CS-305 Data Structures</option>");
										out.println("<option value='CS-306 Computer Programming(Java Technologies)'>CS-306 Computer Programming(Java Technologies)</option>");
										out.println("<option value='CS-308 Seminar/Group Discussion'>CS-308 Seminar/Group Discussion</option>");
									}
								if(semester.equals("4th"))
									{
										out.println("<option value='CS-403 Object Oriented Technology'>CS-403 Object Oriented Technology </option>");
										out.println("<option value='CS-404 Analysis & Design of Algorithm'>CS-404 Analysis & Design of Algorithm </option>");
										out.println("<option value='CS-405 Analog & Digital Communication'>CS-405 Analog & Digital Communication </option>");
										out.println("<option value='CS-406 Computer Programming –IV (.Net Technologies)'></option>");
										out.println("<option value='CS-408 Seminar / Group Discussion'>CS-408 Seminar / Group Discussion</option>");
									}
								if(semester.equals("5th"))
									{
										out.println("<option value='CS-502 Operating System'>CS-502 Operating System</option>");
										out.println("<option value='CS-503 Data Base Management System'>CS-503 Data Base Management System</option>");
										out.println("<option value='CS-504 Computer Graphics & Multimedia'>CS-504 Computer Graphics & Multimedia</option>");
										out.println("<option value='CS-506 Computer Programming V (Unix/Linux Lab.)'>CS-506 Computer Programming V (Unix/Linux Lab.)</option>");
										out.println("<option value='CS-508 Seminar/Group Discussion'>CS-508 Seminar/Group Discussion etc.</option>");
									}
								if(semester.equals("6th"))
									{
										out.println("<option value='CS-601 Micro Processor and Interfacing'>CS-601 Micro Processor and Interfacing</option>");
										out.println("<option value='CS-603 Software Engineering & Project managements'>CS-603 Software Engineering & Project managements</option>");
										out.println("<option value='CS-604 Computer Networking'>CS-604 Computer Networking</option>");
										out.println("<option value='CS-606 Minor Project – I'>CS-606 Minor Project – I</option>");
										out.println("<option value='CS-608 Seminar/Group Discussion'>CS-608 Seminar/Group Discussion</option>");
									}
								if(semester.equals("7th"))
									{
										out.println("<option value='CS-701 Compiler Design'>CS-701 Compiler Design</option>");
										out.println("<option value='CS-703 Cloud Computing'>CS-703 Cloud Computing</option>");
										out.println("<option value='CS-705 Industrial Training'>CS-705 Industrial Training</option>");
										out.println("<option value='CS-706 Major Project'>CS-706 Major Project</option>");
										out.println("<option value='CS-708 Seminar/Group Discussion'>CS-708 Seminar /Group Discussion</option>");
									}
								if(semester.equals("8th"))
									{
										out.println("<option value='CS-801 Soft Computing'>CS-801 Soft Computing</option>");
										out.println("<option value='CS-802 Web Engineering'>CS-802 Web Engineering</option>");
										out.println("<option value='CS-803 Major Project'>CS-803 Major Project</option>");
										out.println("<option value='CS-805 Seminar/Group Discussion'>CS-805 Seminar/Group Discussion</option>");
									}
				
							}
								
						if(branch.equals("CS") && type.equals("T"))
							{
								if(semester.equals("3rd"))
									{
										out.println("<option value='BE-301 Mathematics -II'>BE-301 Mathematics -II</option>");
										out.println("<option value='CS-302 Discrete Structures'>CS-302 Discrete Structures</option>");
										out.println("<option value='CS-303 Digital Circuit & System'>CS-303 Digital Circuit & System</option>");
										out.println("<option value='CS-304 Electronics Devices & Circuits'>CS-304 Electronics Devices & Circuits</option>");
										out.println("<option value='CS-305 Data Structures'>CS-305 Data Structures</option>");
									}
								if(semester.equals("4th"))
									{
										out.println("<option value='BE-401 Mathematics -III'>BE-401 Mathematics -III</option>");
										out.println("<option value='CS-402 Computer System Organization'>CS-402 Computer System Organization</option>");
										out.println("<option value='CS-403 Object Oriented Technology'>CS-403 Object Oriented Technology</option>");
										out.println("<option value='CS-404 Analysis & Design of Algorithm'>CS-404 Analysis & Design of Algorithm</option>");
										out.println("<option value='CS-405 Analog & Digital Communication'>CS-405 Analog & Digital Communication</option>");
									}
								if(semester.equals("5th"))
									{
										out.println("<option value='CS-501 Data Communication'>CS-501 Data Communication</option>");
										out.println("<option value='CS-502 Operating System'>CS-502 Operating System</option>");
										out.println("<option value='CS-503 Data Base Management System'>CS-503 Data Base Management System</option>");
										out.println("<option value='CS-504 Computer Graphics & Multimedia'>CS-504 Computer Graphics & Multimedia</option>");
										out.println("<option value='CS-505 Theory Of Computation'>CS-505 Theory of Computation</option>");
									}
								if(semester.equals("6th"))
									{
										out.println("<option value='CS-601 Micro Processor and Interfacing'>CS-601 Micro Processor and Interfacing</option>");
										out.println("<option value='CS-602 PPL'>CS-602 PPL</option>");
										out.println("<option value='CS-603 Software Engineering & Project Managements'>CS-603 Software Engineering & Project managements</option>");
										out.println("<option value='CS-604 Computer Networking'>CS-604 Computer Networking</option>");
										out.println("<option value='CS-605 Advance Computer Architecture'>CS-605 Advance Computer Architecture</option>");
									}
								if(semester.equals("7th"))
									{
										out.println("<option value='CS-701 Compiler Design'>CS-701 Compiler Design</option>");
										out.println("<option value='CS-702 Distributed System'>CS-702 Distributed System</option>");
										out.println("<option value='CS-703 Cloud Computing'>CS-703 Cloud Computing</option>");
										out.println("<option value='CS-704 Information Storage & Management'>CS-704 Information Storage & Management</option>");
										out.println("<option value='CS-7101 Network & Web Security'>CS-7101 Network & Web Security</option>");
									}
								if(semester.equals("8th"))
									{
										out.println("<option value='CS-801 Soft Computing'>CS-801 Soft Computing</option>");
										out.println("<option value='CS-802 Web Engineering'>CS-802 Web Engineering</option>");
										out.println("<option value='Elective-III'>Elective-III</option>");
										out.println("<option value='Elective-IV'>Elective-IV</option>");
									}
								}
							
							
							if(branch.equals("IT") && type.equals("P"))
								{
								if(semester.equals("3rd"))
									{
										out.println("<option value='IT-303 OOPS Methodology'>IT-303 OOPS Methodology</option>");
										out.println("<option value='IT-304 Electronics Devices & Circuits'>IT-304 Electronics Devices & Circuits</option>");
										out.println("<option value='IT-305 Data Structure & Algorithm'>IT-305 Data Structure & Algorithm</option>");
										out.println("<option value='IT-306 Java Technology'>IT-306 Java Technology</option>");
										out.println("<option value='IT-308 Seminar/Group Discussion'>IT-308 Seminar/Group Discussion</option>");
									}
								if(semester.equals("4th"))
									{
										out.println("<option value='IT-403 Data Base Management System'>IT-403 Data Base Management System</option>");
										out.println("<option value='IT-404 Analysis Design & Algorithm'>IT-404 Analysis Design & Algorithm</option>");
										out.println("<option value='IT-405 Analog Digital Comm.'>IT-405 Analog Digital Comm.</option>");
										out.println("<option value='IT-406 Dot net'>IT-406 Dot net</option>");
										out.println("<option value='IT-408 Seminar/Group Discussion'>IT-408 Seminar/Group Discussion</option>");
									}
								if(semester.equals("5th"))
									{
										out.println("<option value='IT-501 Data Communication'>IT-501 Data Communication</option>");
										out.println("<option value='IT-503 Computer Networks'>IT-503 Computer Networks</option>");
										out.println("<option value='IT-504 System Programming and Operating System'>IT-504 System Programming and Operating System</option>");
										out.println("<option value='IT-506 JAVA Programming'>IT-506 JAVA Programming</option>");
								 		out.println("<option value='IT-508 Seminar/Group Discussion'>IT-508 Seminar/Group Discussion</option>");
									}
								if(semester.equals("6th"))
									{
										out.println("<option value='IT-601 Distributed Systems'>IT-601 Distributed Systems</option>");
										out.println("<option value='IT-602 Computer Graphics & Multimedia'>IT-602 Computer Graphics & Multimedia</option>");
										out.println("<option value='IT-604 Web Technology'>IT-604 Web Technology</option>");
										out.println("<option value='IT-606 Software Engineering Lab'>IT-606 Software Engineering Lab</option>");
										out.println("<option value='IT-608 Seminar/Group Discussion'>IT-608 Seminar/Group Discussion</option>");
 									}
								if(semester.equals("7th"))
									{
										out.println("<option value='IT-701 Object Oriented Analysis & Design'>IT-701 Object Oriented Analysis & Design</option>");
										out.println("<option value='IT-703 Information Storage & Management'>IT-703 Information Storage & Management</option>");
										out.println("<option value='IT-704 Major Project-I'>IT-704 Major Project-I</option>");
										out.println("<option value='IT-705 Industrial Training'>IT-705 Industrial Training</option>");
										out.println("<option value='IT-707 Seminar/Group Discussion'>IT-707 Seminar/Group Discussion</option>");
									}
								if(semester.equals("8th"))
									{
										out.println("<option value='IT-801 Information Security'>IT-801 Information Security</option>");
										out.println("<option value='IT-802 Soft Computing'>IT-802 Soft Computing</option>");
										out.println("<option value='IT-803 Major Project-II'>IT-803 Major Project-II</option>");
										out.println("<option value='IT-805 Seminar/Group Discussion etc.'>IT-805 Seminar/Group Discussion etc.</option>");
									}
							}
							
							if(branch.equals("IT") && type.equals("T"))
								{
								if(semester.equals("3rd"))
									{
										out.println("<option value='BE-301 Mathematics -II'>BE-301 Mathematics -II</option>");
										out.println("<option value='IT-302 Discrete Structure'>IT-302 Discrete Structure</option>");
										out.println("<option value='IT-303 OOPS Methodology'>IT-303 OOPS Methodology</option>");
										out.println("<option value='IT-304 Electronics Devices & Circuits'>IT-304 Electronics Devices & Circuits</option>");
										out.println("<option value='IT-305 Data Structure & Algorithm'>IT-305 Data Structure & Algorithm</option>");
									}
								if(semester.equals("4th"))
									{
										out.println("<option value='BE-401 Mathematics -III'>BE-401 Mathematics -III</option>");
										out.println("<option value='IT-402 Computer System Organization'>IT-402 Computer System Organization</option>");
										out.println("<option value='IT-403 Data Base Management System'>IT-403 Data Base Management System</option>");
										out.println("<option value='IT-404 Analysis Design & Algorithm'>IT-404 Analysis Design & Algorithm</option>");
										out.println("<option value='IT-405 Analog Digital Comm.'>IT-405 Analog Digital Comm.</option>");
									}
								if(semester.equals("5th"))
							 		{
								 		out.println("<option value='IT-501 Data Communication'>IT-501 Data Communication</option>");
								 		out.println("<option value='IT-502 IT Enabled Services, Ethics and Management'>IT-502 IT Enabled Services, Ethics and Management</option>");
								 		out.println("<option value='IT-503 Computer Networks'>IT-503 Computer Networks</option>");
								 		out.println("<option value='IT-504 System Programming and Operating System'>IT-504 System Programming and Operating System</option>");
								 		out.println("<option value='IT-505 JAVA Programming'>IT-505 JAVA Programming</option>");
								 	}
								if(semester.equals("6th"))
									{
										out.println("<option value='IT-601 Distributed Systems'>IT-601 Distributed Systems</option>");
										out.println("<option value='IT-602 Computer Graphics & Multimedia'>IT-602 Computer Graphics & Multimedia</option>");
										out.println("<option value='IT-603 Internet Technology & Network Management'>IT-603 Internet Technology & Network Management</option>");
										out.println("<option value='IT-604 Web Technology'>IT-604 Web Technology</option>");
										out.println("<option value='IT-605 Software Engineering & Project Management'>IT-605 Software Engineering & Project Management</option>");
									}
								if(semester.equals("7th"))
									{	
										out.println("<option value='IT-701 Object Oriented Analysis & Design'>IT-701 Object Oriented Analysis & Design</option>");
										out.println("<option value='IT-702 Wireless and Mobile Computing'>IT-702 Wireless and Mobile Computing</option>");
										out.println("<option value='IT-703 Information Storage & Management'>IT-703 Information Storage & Management</option>");
										out.println("<option value='Elective I'>Elective I</option>");
										out.println("<option value='Elective II'>Elective II</option>");
									}
								if(semester.equals("8th"))
									{	
										out.println("<option value='IT-801 Information Security'>IT-801 Information Security</option>");
										out.println("<option value='IT-802 Soft Computing'>IT-802 Soft Computing</option>");
										out.println("<option value='Elective III'>Elective III</option>");
										out.println("<option value='Elective-IV'>Elective-IV</option>");
									}
								}
							
							out.println("</select></td></tr>");
					
							PreparedStatement stmt=con.prepareStatement("select * from user_info where user_name!='Admin' order by first_name,last_name");
							ResultSet rs=stmt.executeQuery();
					
								if(rs.next())
									{
									out.println("<tr><td>Faculty</td><td> <select name='userId' >");
									out.println("<option value="+rs.getString(1)+">"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");
						
									while(rs.next())
										{
											out.println("<option value="+rs.getString(1)+">"+rs.getString(2)+"	 "+rs.getString(3)+"</option>");	
										}
					
									out.println("</select></td></tr>");
									out.println("<tr><td colspan='2' align='center'><input type='submit' value='Add'></td></tr></table></form>");
									}
								else
									{
									out.println("No User");
									}
							}
				}catch(Exception e)
					{
						out.print("<script>alert('Unable To Select Subject');</script>");
						System.out.println("Exception by Add Subject Page"+e);
						RequestDispatcher rd=request.getRequestDispatcher("ViewSubjects");
						rd.forward(request, response);
					}	
			out.print("<a href='adminLogin.jsp'>Home</a>");
			out.println("</body></html>");
	}
}
