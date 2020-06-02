package src.com.weather.forecast.report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		String forecastUrl;

		forecastUrl = LongitudeAndLatitudePointsApi.getPointsAPI(latitude, longitude);

		System.out.println(forecastUrl);

		LongitudeAndLatitudePointsApi.getForecastAPI(forecastUrl);

	}

	private static String getPointsAPI(double latitude, double longitude) {

		String forecastURL = "";
		try {
			Map<Object, Object> hmap = new HashMap<>();

			URL urlForGetRequest = new URL("https://api.weather.gov/points/" + latitude + "," + longitude);

			
			//calling helper method for connecting to webservice and getting the response
			String response = jsonResponse(urlForGetRequest);

			JSONParser parse = new JSONParser();

			JSONObject jobj = (JSONObject) parse.parse(response);

			Object points = jobj.get("geometry");

			System.out.println(points);
			Map forecast = (Map) jobj.get("properties");

			Iterator<Map.Entry> itr1 = forecast.entrySet().iterator();

			while (itr1.hasNext()) {

				Map.Entry pair = itr1.next();
				if (pair.getKey().equals("forecast")) {
					System.out.println(pair.getKey() + " : " + pair.getValue());
					hmap.put(pair.getKey(), pair.getValue());
					forecastURL = pair.getValue().toString();
				}
			}
		} catch (MalformedURLException | ParseException e) {
			e.printStackTrace();
		}

		
		//sending the forecast url from the points api
		return forecastURL;

	}

	private static void getForecastAPI(String forecastUrl) {

		List list = new ArrayList<>();
		
		LocalDateTime today = LocalDateTime.now();

		try {

			System.out.println("Get Fore Cast API ");
			System.out.println(forecastUrl);

			URL urlForGetRequest = new URL(forecastUrl);

			//calling helper method for connecting to webservice and getting the response
			String response = jsonResponse(urlForGetRequest);

			JSONParser parse = new JSONParser();

			JSONObject jobj = (JSONObject) parse.parse(response);

			Map periods = (Map) jobj.get("properties");

			Iterator<Map.Entry> itr1 = periods.entrySet().iterator();

			while (itr1.hasNext()) {

				Map.Entry pair = itr1.next();
				if (pair.getKey().equals("periods")) {
						list.add(pair.getValue());
				}
			}
			
			
			// This list is printing all the records of all the days
			System.out.println(list);

          //Will work on getting five days report
			
		} catch (MalformedURLException | ParseException e) {
			e.printStackTrace();
		}

	}

	private static String jsonResponse(URL urlForGetRequest) {

		StringBuffer response = new StringBuffer();

		try {

			System.out.println(urlForGetRequest);
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			System.out.println(responseCode);
			String readLine = null;

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}

}
