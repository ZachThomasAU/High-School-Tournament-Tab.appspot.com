package com.stdesco.swisstab.apicode;

/**
 * Copyright (C) Standard Esports Company - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the copyright holder.
 * 
 * Generate Provider ID
 * 
 * @author JLwin
 * @author ZThomas
 * January 2019
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(
		name = "Tournament Initialiser", 
		value = "/InitialisationPost"
)
public class InitialisationPost extends HttpServlet {
	/**
	 * Code generates the Provider ID, and then creates a new Tournament.
	 * 
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	
	private final static Logger LOGGER = 
			Logger.getLogger(InitialisationPost.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		// InitialisationPost http = new InitialisationPost();
		// System.out.println("Testing 1 - Send Http GET request");
		// http.sendGet();
		
		System.out.println("We in boyz");
		
		// creates variables for API urls
		String xriottoken = "RGAPI-372193cf-17e2-4d61-81ba-d75f5053ebc0";
		String retweb = 
				"https://high-school-tournament-tab.appspot.com/Hello.jsp";
		
		// System.out.println("\nTesting 2 - Send Http POST request");
		
		/* Creating and initialising a new provider code for the application*/ 
		Provider prov = new Provider();		
		try {
			prov.init_Provider(retweb, xriottoken, "OCE");
		} catch (Exception e) {
			// TODO FIX THIS QUICKLY AH GOD SAVE ME NO!
			e.printStackTrace();
			LOGGER.warning("This is not supposed to happen! - Provider");
		}
		// Reports success, and the Provider ID to the user
		PrintWriter out = resp.getWriter();
		out.println("<b>PROVIDER ID:</b> " + prov.get_ProviderId() + "<br />");
		out.println("Now requesting Tournament ID <br /> <br />");
		
		// Creating and initialising a new tournamentID for the application
		Tournament tour = new Tournament();
		String tournamentName = req.getParameter("name");
		try {
			tour.init_Tournament(xriottoken, tournamentName, 
					prov.get_ProviderId());
		} catch(Exception e) {
			// TODO FIX THIS QUICKLY AH GOD SAVE ME NO!
			e.printStackTrace();
			LOGGER.warning("This is not supposed to happen! - Tournament");
		}
		
		//Reports success, and the Tournament ID to the user
		out.println("<b>TOURNAMENT ID:</b> " + tour.get_TournamentId() + 
					"<br />");
		out.println("<b>PLEASE RECORD YOUR TOURNAMENT ID. USER ACCOUNTS WITH "
				+ "STORAGE COMING SOON <br /> <br />");
		out.println("<a href= \"index.html\">Return Home</a>");
		
		return;
	}

	/**
	 * 
	 * @throws Exception
	 */
	// I need notes for this. WTF does this mean? @author ZThomas
	/** public void sendGet() throws Exception {
		// HTTP GET request

		String url = "http://www.google.com/search?q=mkyong";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	} */
	
	/**
	 * Function to request POST API from riot STUB
	 * input: 
	 * 
	 * @param xriotkey
	 * @param BodyInput
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public StringBuffer sendPostApi(String xriotkey, String BodyInput, 
									String url) throws Exception {

		// Http address of the the riot API stub 
		// String url = "https://americas.api.riotgames.com/lol/
		//				+ "tournament-stub/v4/providers";
		
		// Object that contains information about the URL location
		URL obj = new URL(url);	
		
		// Connection object opens connection to the url
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		/* Set the Request Method */
		con.setRequestMethod("POST");
		
		/* HEADER INFORMATION IS INPUT HERE
		 * con.setRequestProperty sets the Header values for the POST Request
		 * 
		 * Accept Charset is just a standard value
		 * 
		 * XRiot Token is the unique account identifier that is generated for 
		 * the Lol Account 
		 * 
		 * Accept Language = Standard language that was copied from the example 
		 * in the STUB
		 * 
		 * Origin =  Currently set to the computer near the door however i 
		 * tried with it blank and it still worked
		 * 
		 * Content type = JSON because that is the format the STUB example uses 
		 * and is passed into the body through the below string urlparameters
		 * */
		con.setRequestProperty("Accept-Charset", 
				"application/x-www-form-urlencoded; charset=UTF-8");
		con.setRequestProperty("X-Riot-Token", xriotkey);	
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.9");	
		con.setRequestProperty("User-Agent", "Mozilla/5.0 "
				+ "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
				+ "(KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		con.setRequestProperty("Origin", "130.102.10.13");
		con.setRequestProperty("Content-Type","application/json");
		
		String urlParameters = BodyInput;
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		System.out.println(wr.toString());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		//System.out.println(response.toString());
		
		return response;
	}

}
