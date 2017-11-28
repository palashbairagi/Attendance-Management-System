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
<title>Change Password</title>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="jquery.validate.js"></script>
<script>
function validatePassword(){	
	var validator = $("#changePassword").validate({
		rules: {        	   
			currentPassword: "required",
			newPassword :"required",
			confirmPassword:{
				equalTo: "#newPassword"
		    }		
    	},  	                        
	    messages: {
	    	currentPassword: "Enter Current Password",
    		newPassword :"Enter New Password",
    		confirmPassword :"Password do not match"
	    }
	});
	if(validator.form()){
		alert('Sucess');
	}
}
</script>
<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
</head>
<body>
<form action="ChangePassword" name='changePassword' id='changePassword'>
<table>
	<tr>
		<td>Current Password
		<td><input type="password" name="currentPassword" id='currentPassword'>
	</tr>
	<tr>
		<td>New Password
		<td><input type="password" name="newPassword" id='newPassword'>
	</tr>
	<tr>
		<td>Confirm Password
		<td><input type="password" name="confirmPassword" id="confirmPassword">
	</tr>
	<tr>
		<td colspan=2 align="center"><input type="submit" value="Change Password" onClick="validatePassword();">
	</tr>
</table>
</form>
</body>
</html>