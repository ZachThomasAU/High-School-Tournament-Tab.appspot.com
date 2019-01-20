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
import java.io.InputStreamReader;
import java.net.URL;
//import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class SendPostAPI extends HttpServlet {

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
		
		//System.out.println(wr.toString());
		
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
