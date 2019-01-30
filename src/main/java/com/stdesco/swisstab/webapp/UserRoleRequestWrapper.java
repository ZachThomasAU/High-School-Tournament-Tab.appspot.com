package com.stdesco.swisstab.webapp;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * User Role Request Wrapper Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class UserRoleRequestWrapper extends HttpServletRequestWrapper {

	private String user;
	private List<String> roles = null;
	private HttpServletRequest realRequest;
	
	/**
	 * 
	 * @param user
	 * @param roles
	 * @param request
	 */
	public UserRoleRequestWrapper(String user, List<String> roles, 
								  HttpServletRequest request) {
		super(request);
		this.user = user;
		this.roles = roles;
		this.realRequest = request;
	}
	
	@Override
	public boolean isUserInRole(String role) {
		if (roles == null) {
			return this.realRequest.isUserInRole(role);
		}
		return roles.contains(role);
	}
	
	public Principal getUserPrinciple() {
		if (this.user == null) {
			return realRequest.getUserPrincipal();
		}
		
		return new Principal() {
			@Override
			public String getName() {
				return user;
			}
		};
	}

}
