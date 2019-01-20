<script>

// AJAX Pass POST and return from servlet
	
	$('#testButton').click(function(){			
		//alert('Getting Provider');
		
		$.ajax({
		url: 'provider',		
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			
			//on success the AJAX runs this callback function and you can 
			//manipulate the data on the screen in a variety of ways 
			
			success: function(data){
				
				//If data is not valid because the response had isValid=False
				//then it will throw an alert which can be handled here
				
				if(data.isValid){
					$('#providerVal').html('Provider: ' + data.provider);
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