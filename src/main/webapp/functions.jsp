<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<script>

	// AJAX Pass POST and return from servlet
	
	$('#updateUsername').submit(function(){			
		alert('init Provider');		
			$.ajax({
		url: 'provider',
		
			type: 'POST',
			dataType: 'json',
			data: $('#updateUsername').serialize(),
			success: function(data){
				
				if(data.isValid){
					$('#displayName').html('your name is: ' + data.username);
					$('#displayName').slideDown(500);
				}else{
						alert('Please enter a valid username!!');	
				}
			}	
		});
		return false;
	});
	
	$('#providerButton').click(function(){			
		$.ajax({
		url: 'provider',
		
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data){
				
				if(data.isValid){
					$('#displayProvider').html('your name is: ' + data.username);
					$('#displayProvider').slideDown(500);
					
				}else{
						alert('Please enter a valid username!!');	
				}
			}	
		});		
	return false;
	});	
	
	$('#initDatastore').click(function(){			
		$.ajax({
		url: 'initDatastore',	
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data){				
				if(data.isValid){				
					
					$('#displayInit').html(data.response);
					$('#displayInit').slideDown(500);
					
				}else{
					
						alert('Please enter a valid username!!');	
				}
			}	
		});		
	return false;
	});	
	

	</script>


</body>
</html>