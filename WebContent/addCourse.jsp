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
<title>View Course</title>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
</head>
<body>
<form action="AddCourse" method="get">
<table>
	<tr>
		<td><label>Branch</label></td>
		<td><select name="branch">
				<option value="CS">CS</option>
				<option value="IT">IT</option>
				<option value="EC">EC</option>
				<option value="CE">CE</option>
				<option value="ME">ME</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><label>Semester</label></td>
		<td><select name="semester">
				<option value="3rd">3rd</option>
				<option value="4th">4th</option>
				<option value="5th">5th</option>
				<option value="6th">6th</option>
				<option value="7th">7th</option>
				<option value="8th">8th</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Section
		<td><input type="text" name='section'>
	</tr>
	<tr>
		<td colspan=2 align="center"><input type="submit" value="Add" /></td>
	</tr>
</table>
<a href='adminLogin.jsp'>Home</a>
</form>
</body>
</html>