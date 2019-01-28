<script>

// AJAX Pass POST and return from servlet

	$('#createPairing').click(function() {
		
		alert("Create Pairing")
		
		$.ajax( {
			url: 'CreatePairing',	
			type: 'POST',
			dataType: 'json',
			data: 'tournamentid=4579',
			success: function(data) {				
				if (data.respcode == 4) {
					
					alert('Successfully paired' + data.respcode);
					
					//$('#returnStr').load(data.gameinfo);
					
				} else {
					
				    alert('response: ' + data.respcode);	
				    
				}
			}	
		} );		
		
	return false;
	});
	
	
	$('#tournamentTest').click(function() {				
		$.ajax( {
			url: 'tournamentCodeTest',	
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data) {				
				if(data.isValid) {								
					$('#tournamentVal').html("Tournament ID:" + data.tournament);
					$('#tournamentVal').slideDown(500);			
				} else {				
				   alert('Please enter a valid username!!');	
				}
			}	
		} );		
		
	return false;
	} );
	
	
	$('#testProvider').click(function() {	
		
		// Send post request to -> providerServlet
		
		$.ajax( {
			url: 'provider',		
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			
			//on success the AJAX runs this callback function and you can 
			//manipulate the data on the screen in a variety of ways 
			
			success: function(data) {
				
				//If data is not valid because the response had isValid=False
				//then it will throw an alert which can be handled here
				
				if (data.isValid) {
					$('#providerVal').html('Provider: ' + data.provider);
					$('#providerVal').slideDown(500);
					
				} else {
						alert('Please enter a valid username!!');	
				}
			}	
		} );		
	return false;
	} );	
	
	
	$('#initDatastore').click(function() {				
		$.ajax( {
			url: 'initDatastore',	
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data) {				
				if (data.isValid) {				
					
					$('#displayInit').html(data.response);
					$('#displayInit').slideDown(500);
					
				} else {
					
						alert('Please enter a valid username!!');	
				}
			}	
		} );		
		
	return false;
	});
	
	$('#initDummyData').click(function() {				
		$.ajax( {
			url: 'initDummyData',	
			type: 'POST',
			dataType: 'json',
			data: 'FirstName=Mickey&LastName=Mouse',
			success: function(data) {				
				if (data.isValid) {				
					
					$('#displayDummy').html(data.response);
					$('#displayDummy').slideDown(500);
					
				} else {
					
						alert('ehrm...something is wrong');	
				}
			}	
		} );		
		
	return false;
	} );
	

</script>