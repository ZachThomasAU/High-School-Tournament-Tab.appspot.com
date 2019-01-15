package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Initiate Swiss Class
 * 
 * @author ZThomas
 * January 2019
 */

@SuppressWarnings("serial")
@WebServlet(
		name = "Tournament Start",
		value = "/start")
public class TournamentStartPost extends HttpServlet {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
			Logger.getLogger(InitialisationPost.class.getName());

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated constructor stub
	}

}
