<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html  charset=ISO-8859-1">
<script src="jquery-1.9.1.js"></script>
<script src='jquery-1.10.2.js'></script>  
<script src='jquery-ui.js'></script>  
<script src='selectDateToViewAttendance.js'></script>
<script src="jquery.validate.min.js"></script>
<script src="bootstrap.min.js"></script>
<script type='text/javascript' src='validation.js'></script>  
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
<link rel='stylesheet' href='jquery-ui.css'>
</head>
<body>
<form action='ViewStudentTotalAttendance' id='viewStudentTotalAttendance' name='viewStudentTotalAttendance'>
<table>
	<tr>
		<td>Enrollment Number</td>
		<td><input type='text' name='rollNumber' id='rollNumber'/></td>
	</tr>
	<tr>
		<td>Date From</td>
		<td><input type='text' class='datepicker' name='dateFrom' id='dateFrom'></td>
	</tr>
    <tr>
    	<td>Date To</td>
    	<td><input type='text' class='datepicker' name='dateTo' id='dateTo'></td>
	</tr>
	<tr>
		<td colspan='2' align='center'><input type='submit' value='Go'></td>
	</tr>
</table>
<a href="loginPage.jsp">Home</a>
</form>				
</body>
</html>