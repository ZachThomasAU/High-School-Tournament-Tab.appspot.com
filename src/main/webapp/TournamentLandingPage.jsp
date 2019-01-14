<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Tournament Setup</title>
</head>
<body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
	<h2>Input the setup data for a tournament prior to round 1</h2>
	
	<form method="POST" action="/initSwiss">
	
		<div>
			<label for="tid">Tournament ID</label> <input type="text" 
				name="tid" id="tid" size="40" 
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>
		
		<div>
			<label for="rounds">Number of Rounds</label> <input type="text" 
				name="rounds" id="rounds" size="40" 
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>
		
		<div>
			<label for="teams">Number of Teams</label> <input type="text" 
				name="teams" id="teams" size="40" 
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>
		
		<div>
			<label for="firstRoundPairingRule">Pair first round randomly?
				</label> <input type="checkbox" name="firstRoundPairingRule" 
				id="firstRoundPairingRule" size="40" 
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>
		
		<button type="submit">Create</button>
		<button type="button">Exit</button> <!-- TODO: Back button --> 
	</form>
</div>
</body>
<footer>
	<!-- <small>
	Copyright (C) Standard Esports Company - All Rights Reserved
	</small> -->
</footer>
</html>