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
<title>Welcome</title>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
<script type="text/javascript" src="validation.js"></script>
</head>
<body>
<a href="SelectSubjectToAddAttendance">Add Attendance</a><br>
<a href="SelectSubjectAndDateToViewAttendance">View Attendance</a><br>
<a href="changePassword.jsp">Change Password</a><br>
<a href='SignOut'>Sign Out</a>
</body>
</html>