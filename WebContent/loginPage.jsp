<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
<script src="bootstrap.min.js"></script>
<script src="jquery-1.9.1.js"></script>
<script src="jquery.validate.min.js"></script>
<script type="text/javascript" src="validation.js"></script>
</head>
<body>
<form action="Login" method="post" name='loginForm' id='loginForm'>
<table style="width:800px">
	<tr>
		<td>User Name</td>
		<td><input type="text" name="userName" /></td>
		<td><span id='userName' ></span></td>
	</tr>
	<tr>
		<td>Password</td>
		<td><input type="password" name="password" /></td>
		<td><span id='password'></span></td>
	</tr>
	<tr>	
		<td colspan=3 align="center"><input type="submit" value="Login" />
</table>
</form>
<a href='viewStudentTotalAttendance.jsp'>View Attendance</a>
</body>
</html>