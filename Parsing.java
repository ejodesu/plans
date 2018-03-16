package me.yeon.ejodesu.parsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Parsing {

	public void getShobosho() {
		try {
			Document doc = Jsoup.connect("https://www.navitime.co.jp/category/0502003/").get();
			System.out.println(doc.title());
			Elements anchors = doc.select("dt.spot_name a");
			Elements addresses = doc.select("dd.address_name");
			String address = null;
			for (int i = 0; i < anchors.size(); i++) {
				System.out.println(String.format("%s\n\t%s", anchors.get(i).text(), anchors.get(i).absUrl("href")));
				System.out.println(String.format("\t%s", addresses.get(i).ownText(), addresses.get(i).absUrl("href")));
				address = addresses.get(i).ownText();
				break;
			}
			
			getPlace(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	final String googleuri = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
			+"location=35.6,139.7"
			+"&radius=500000"
			+"&keyword=%s"
			+ "&key=AIzaSyBCeYc6t2cCmLBo9xG8Oow2dYEwmv9U6Cg";
	public void getPlace(String target) {
		// AIzaSyBCeYc6t2cCmLBo9xG8Oow2dYEwmv9U6Cg
		//String
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(String.format(googleuri, URLEncoder.encode(target, "UTF-8")));
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			
			String result = response.toString();
			
			Gson g = new Gson();
			JsonParser p = new JsonParser();
			
			System.out.println(result);
			
			JsonObject loc = p.parse(result).getAsJsonObject().get("results").getAsJsonArray().get(0)
					.getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
			double lat = loc.get("lat").getAsDouble();
			double lng = loc.get("lng").getAsDouble();
			
			System.out.println(String.format("%f,%f",lat,lng));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
}
