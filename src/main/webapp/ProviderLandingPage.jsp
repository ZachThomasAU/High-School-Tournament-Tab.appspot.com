<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>New Provider ID</title>
</head>
<body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
	<h2>Generate a Tournament</h2>
	
	<form method="POST" action="/InitialisationPost">
	
		<div>
			<label for="name">Tournament Name</label> <input type="text"
			name="name" id="name" size="40"
			value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>
	
		<button type="submit">Generate a new tournament!</button>
		<button type="button">Back</button> <!-- TODO: Back button --> 
	</form>
</div>
</body>
</html>