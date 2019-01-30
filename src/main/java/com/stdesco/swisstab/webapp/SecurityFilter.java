package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stdesco.swisstab.bean.UserAccount;
import com.stdesco.swisstab.utils.SecurityUtils;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Security Filter Class
 * 
 * @author zthomas
 * January 2019
 *
 */

@WebFilter("/*")
public class SecurityFilter implements Filter {

	public SecurityFilter() {
		// Is this required?
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// This is required for implements Filter. Have no idea why.
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpreq = (HttpServletRequest) request;
		HttpServletResponse httpresp = (HttpServletResponse) response;
		
		String servletPath = httpreq.getServletPath();
		
		UserAccount loggedInUser = 
							AppUtils.getLoggedInUser(httpreq.getSession());
		
		if (servletPath.equals("/login")) {
			chain.doFilter(httpreq, httpresp);
			return;
		}
		HttpServletRequest wrapRequest = httpreq;
		
		if (loggedInUser != null) {
			// Username
			String username = loggedInUser.getUsername();
			// Roles
			List<String> roles = loggedInUser.getRoles();
			// Wrap old request by a new request with username and role info
			wrapRequest = new UserRoleRequestWrapper(username, roles, httpreq);
		}
		
		if (SecurityUtils.isSecurityPage(httpreq)) {
			// If the user is not logged in, redirect to the login page.
			if (loggedInUser == null) {
				String requestURI = httpreq.getRequestURI();
				
				// Store the current page to redirect back to, after login.
				int redirectID = AppUtils.storeRedirectAfterLoginURL(
											httpreq.getSession(), requestURI);
				
				httpresp.sendRedirect(wrapRequest.getContextPath() 
									  + "/login?redirectID=" + redirectID);
				return;
			}
		}
		
		chain.doFilter(wrapRequest, httpresp);
	}

	@Override
	public void destroy() {
		// This is required for implements Filter. Have no idea why.
		
	}

}
