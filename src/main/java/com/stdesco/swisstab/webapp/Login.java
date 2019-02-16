package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/login")
public class Login extends HttpServlet {
	private final static Logger LOGGER = 
		 						Logger.getLogger(Login.class.getName());
	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		//init
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (Validate.checkUser(username, password)) {
			RequestDispatcher requestDispatcher = 
										request.getRequestDispatcher("index.html");
			requestDispatcher.forward(request, response);
		} else {
			LOGGER.log(Level.INFO, "ln 33: Username or Password incorrect");
			out.println("<span style=\"color: red;\"> Username or Password "
					+ "incorrect </span>");
			RequestDispatcher requestDispatcher = 
									request.getRequestDispatcher("login.html");
			requestDispatcher.include(request, response);
		}
		
	}

}
