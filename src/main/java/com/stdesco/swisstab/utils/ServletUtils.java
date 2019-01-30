package com.stdesco.swisstab.utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ServletUtils {
	
	/** Public static method for writing GSON data back to the javascript 
	 *  request that has been sent by the webapp
	 * 
	 * @param resp - Object of type HttpServletResponse
	 * @param map - Hashmap with <String, Object> to be converted to JSON
	 * @throws IOException
	 */
	public static void writeback(HttpServletResponse resp, 
			Map<String, Object> map) throws IOException {
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		System.out.print("ServletUtils:18: Sending JSON Response \n");
		
		System.out.print(new Gson().toJson(map).toString() + "\n");
		
		resp.getWriter().write(new Gson().toJson(map));
	}
}
