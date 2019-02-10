<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Info</title>
</head>
<body>
	 <jsp:include page="_menu.jsp"></jsp:include>
 
     <h3>Hello: ${loggedInUser.username}</h3>
 
     User Name: <b>${loggedInUser.username}</b>
     <br />
     Gender: ${loggedInUser.gender } <br />

</body>
</html>