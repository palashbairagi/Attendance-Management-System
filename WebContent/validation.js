$(function() {
  
    $("#dayAttendance").validate({
    
        // Specify the validation rules
        rules: {
        	date:"required",
        },
        
        // Specify the validation error messages
        messages: {
        	date:"Please Enter Date",
        },
        
        submitHandler: function(form) {
            form.submit();
        }
    });

  });


$(function() {
  
    $("#loginForm").validate({
    
        // Specify the validation rules
        rules: {
        	userName:"required",
            password:"required",
        },
        
        // Specify the validation error messages
        messages: {
        	userName:"Please Enter Username",
            password:"Please Enter Password",
        },
        
        submitHandler: function(form) {
            form.submit();
        }
    });

  });
  

  
$(function() {
	  
    $("#viewStudentTotalAttendance").validate({
    
        // Specify the validation rules
        rules: {
        	rollNumber: {
                required: true,
                minlength: 12,
                maxlength: 12
            }, 
            dateFrom:"required",
            dateTo:"required",
        },
        
        // Specify the validation error messages
        messages: {
        	rollNumber:{
                required: "Please Enter Enrollment Number",
                minlength: "Enrollment Number must be 12 characters long",
                maxlength: "Enrollment Number must be 12 characters long"
            },
            dateFrom:"Please Enter Date",
            dateTo:"Please Enter Date",
        },
        
        submitHandler: function(form) {
            form.submit();
        }
    });

  });


$(function() {
	  
    $("#editStudent").validate({
    
        // Specify the validation rules
        rules: {
        	rollNumber: {
                required: true,
                minlength: 12,
                maxlength: 12
            }, 
            name:"required"
        },
        
        // Specify the validation error messages
        messages: {
        	rollNumber:{
                required: "Please Enter Enrollment Number",
                minlength: "Enrollment Number must be 12 characters long",
                maxlength: "Enrollment Number must be 12 characters long"
            },
            name:"Please Enter Name"
        },
        
        submitHandler: function(form) {
            form.submit();
        }
    });

  });



$(function() {
  
    // Setup form validation on the #register-form element
    $("#userRegistration").validate({
    
        // Specify the validation rules
        rules: {
            firstName: "required",
            lastName: "required",
            userName: "required",
            password: {
                required: true,
                minlength: 5
            },
        },
        
        // Specify the validation error messages
        messages: {
            firstName: "Please Enter First Name",
            lastName: "Please Enter Last Name",
            password: {
                required: "Please Enter Password",
                minlength: "Password must be at least 5 characters long"
            },
            userName: "Please Enter Username",
        },
        
        submitHandler: function(form) {
            form.submit();
        }
    });

  });
  

