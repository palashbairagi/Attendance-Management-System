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
<title>View User</title>
<script src="jquery-1.9.1.js"></script>
<script src="jquery.validate.min.js"></script>
<script src="bootstrap.min.js"></script>
<script type="text/javascript" src="validation.js"></script>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
</head>
<body>
<form action="AddUser" method='get' name='userRegistration' id='userRegistration'>
<table>
	<tr>
		<td>First Name</td>
		<td><input type="text" name="firstName" id='firstName'/></td>
	</tr>
	<tr>
		<td>Last Name</td>	
		<td><input type="text" name="lastName" id='lastName'/></td>
	</tr>
	<tr>
		<td>UserName</td>
		<td><input type="text" name="userName" id='userName'/></td>
	</tr>
	<tr>
		<td>Password</td>
		<td><input type="password" name="password" id='password'/></td>
	</tr>
	<tr>
		<td colspan='2' align='center'><input type="submit" value="Add" /></td>
	</tr>
</table>
<a href='adminLogin.jsp'>Home</a>	
</form>
</body>
</html>