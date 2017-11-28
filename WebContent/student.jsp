<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
boolean isValid=other.Getter.isSessionValid(request);
if(!isValid)
	{%>
	<jsp:forward page="loginPage.jsp"></jsp:forward>
	<%}%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student</title>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
</head>
<body>
<a href='AddStudentPage?courseId=null'>Add</a><br>
<a href='SelectCourse'>View</a><br>
<a href='adminLogin.jsp'>Home</a>
</body>
</html>