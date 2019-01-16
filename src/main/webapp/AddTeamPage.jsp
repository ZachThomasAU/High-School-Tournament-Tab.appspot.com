<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add a New Team Here</title>
</head>
<body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">

	<form method="POST" action="/addTeam">
		<h2>Add a New Team</h2>
		<div>
			<label for="tid">Tournament ID</label> <input type="text"
				name="tid" id="tid" size="40"
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>	
		
		<div>
			<label for="tname">Team Name</label> <input type="text"
				name="tname" id="tname" size="40"
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>	
		
		<button type="submit">K GO!!!</button>
		<button type="button">Exit</button> <!-- TODO: Back button -->
	</form>
	
	<form method="POST" action="/removeTeam">
		<h2>Remove an Existing Team</h2>
		<div>
			<label for="tid">Tournament ID</label> <input type="text"
				name="tid" id="tid" size="40"
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>	
		
		<div>
			<label for="tname">Team Name</label> <input type="text"
				name="tname" id="tname" size="40"
				value="${fn:escapeXml(blog.title)}" class="form-control" />
		</div>	
		
		<button type="submit">K GO!!!</button>
		<button type="button">Exit</button> <!--  TODO: Back Button -->
	</form>
</div>
</body>
</html>