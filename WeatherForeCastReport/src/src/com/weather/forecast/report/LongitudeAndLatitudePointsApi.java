package src.com.weather.forecast.report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LongitudeAndLatitudePointsApi {

	public static void main(String[] args) {

		// Using the scanner for getting the parameters from the command line

		Scanner sc = new Scanner(System.in);

		// We are doing this as we have to trying to get the values using one command
		// line parameter
		String str = sc.next();

		System.out.println(str);

		// we are splitting based on the comma separator
		String[] str1 = str.split(",");

		// right i am hard coding the values as i know there are only two values later
		// we
		// can change accordingly
		double latitude = Double.parseDouble(str1[0]);

		double longitude = Double.parseDouble(str1[1]);

		// creating helper method for calling rest api service
		String forecastUrl=LongitudeAndLatitudePointsApi.getPointsAPI(latitude, longitude);
		
		System.out.println(forecastUrl);
		
		LongitudeAndLatitudePointsApi.getForecastAPI(forecastUrl);
		
		

	}

	private static void getForecastAPI(String forecastUrl) {
		
		List list=new ArrayList<>();
		
		
		System.out.println("Get Fore Cast API ");
		System.out.println(forecastUrl);
		
		
		URL urlForGetRequest;
		try {
			urlForGetRequest = new URL(forecastUrl);
			
			System.out.println(urlForGetRequest);
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			System.out.println(responseCode);
			String readLine = null;
			
			StringBuffer response = new StringBuffer();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				// print result
				// System.out.println("JSON String Result " + response.toString());

				JSONParser parse = new JSONParser();

				JSONObject jobj = (JSONObject) parse.parse(response.toString());
				Map periods = (Map)jobj.get("properties");
				
				Iterator<Map.Entry> itr1=periods.entrySet().iterator();
				
				while (itr1.hasNext()) {
					
					 Map.Entry pair = itr1.next(); 
					 if(pair.getKey().equals("periods")) {
			         System.out.println(pair.getKey() + " : " + pair.getValue());
			          list.add( pair.getValue());
					 }
				}
				
				System.out.println(list);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	private static String getPointsAPI(double latitude, double longitude) {

		Map<Object,Object> hmap=new HashMap<>();
		
		String forecastURL="";
		
	
		try {
			// URL urlForGetRequest =new URL("https://api.weather.gov/points/{"+latitude+"},{"+longitude +"}");
			URL urlForGetRequest = new URL("https://api.weather.gov/points/" + latitude + "," + longitude);
			System.out.println(urlForGetRequest);
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			System.out.println(responseCode);
			String readLine = null;
			
			StringBuffer response = new StringBuffer();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				// print result
				// System.out.println("JSON String Result " + str);

				JSONParser parse = new JSONParser();

				JSONObject jobj = (JSONObject) parse.parse(response.toString());

				Object points = jobj.get("geometry");

				System.out.println(points);
				Map forecast = (Map)jobj.get("properties");
				
				Iterator<Map.Entry> itr1=forecast.entrySet().iterator();
				
				while (itr1.hasNext()) {
					
					 Map.Entry pair = itr1.next(); 
					 if(pair.getKey().equals("forecast")) {
			        System.out.println(pair.getKey() + " : " + pair.getValue());
			          hmap.put(pair.getKey(), pair.getValue());
			          forecastURL=pair.getValue().toString();
					 }
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forecastURL;

	}
}
