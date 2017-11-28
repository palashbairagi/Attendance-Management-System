$(document).ready(function(){

$(function(){
var d = new Date();
d.setDate(d.getDate() );
    $( "#datepicker" ).datepicker({ 
    	beforeShowDay: function(date) {
	        var day = date.getDay();
	        return [(day != 0), ''];
	    } ,
			minDate: "d-5"
			,maxDate: "d"
			,dateFormat: 'dd-mm-yy'
			,defaultDate: "d"
		
		});

  });
  
});
