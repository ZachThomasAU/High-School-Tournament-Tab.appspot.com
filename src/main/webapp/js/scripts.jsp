<script>

// AJAX Pass POST and return from servlet
	
	$('#testButton').click(function(){			
		//alert('Getting Provider');
		
		$.ajax({
		url: 'provider',		
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data){
				
				if(data.isValid){
					$('#providerVal').html('Provider: ' + data.username);
					$('#providerVal').slideDown(500);
					
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