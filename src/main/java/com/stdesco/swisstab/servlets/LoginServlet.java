package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stdesco.swisstab.apicode.GameAPI;
import com.stdesco.swisstab.bean.UserAccount;
import com.stdesco.swisstab.utils.DataAccessObject;
import com.stdesco.swisstab.utils.WebAppUtils;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Login Servlet Class
 * 
 * @author zthomas
 * January 2019
 *
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

	public LoginServlet() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = 
				this.getServletContext().getRequestDispatcher(
												"/WEB-INF/views/loginView.jsp");
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserAccount userAccount = DataAccessObject.findUser(username, password);
		
		if (userAccount == null) {
			String errorMessage = "Invalid Username or Password";
			request.setAttribute("errorMessage", errorMessage);
			
			RequestDispatcher dispatcher = 
					this.getServletContext().getRequestDispatcher(
												"/WEB-INF/views/loginView.jsp");
			dispatcher.forward(request, response);
			return;
		}
		
		WebAppUtils.storeLoggedInUser(request.getSession(), userAccount);
		
		int redirectID = -1;
		try {
			redirectID = Integer.parseInt(request.getParameter("redirectID"));
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("LoginServlet ln:74 - We call this ignoring the "
					+ "problem and hoping it goes away...");
		}
		String requestURI = WebAppUtils.getRedirectAfterLoginURL(
											request.getSession(), redirectID);
		if (requestURI != null) {
			response.sendRedirect(requestURI);
		} else {
			response.sendRedirect(request.getContextPath() + "/userInfo");
		}
	}

}
