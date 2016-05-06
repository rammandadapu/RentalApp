package edu.sjsu.cmpe277.rentalapp.rentalapp;

/**
 * Created by divya.chittimalla on 5/4/16.
 */

import android.content.Context;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.net.HttpURLConnection;


public class WebService {

    OAuthService service;
    HttpURLConnection urlConnection;

    public String searchProperties(String keyword, String location, String priceLow, String priceHigh, String type) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://10.0.2.2:1337/searchtest");
        /*request.addQuerystringParameter("keyword", keyword);
        request.addQuerystringParameter("pricelow", priceLow);
        request.addQuerystringParameter("pricehigh", priceHigh);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("type", type);*/
        Response response = request.send();
        return response.getBody();
    }

    public String getPropertyDetails(String _id) {
        String requestStr = "http://10.0.2.2:1337/property/"+_id;
        OAuthRequest request = new OAuthRequest(Verb.GET, requestStr);
        //Dummy code until real API is available - START
        String response = "{\"_id\":\"572835524255d0944af1c63d\",\"address\":{\"line1\":\"1 S Market St Apt 502\",\"city\":\"San Jose\",\"state\":\"CA\",\"zip\":\"95113\"},\"type\":\"house\",\"roomsNo\":3,\"bathNo\":2,\"size\":1440,\"price\":5560,\"phone\":\"(238)-434-676\",\"email\":\"test@mail.com\",\"desc\":\"Large spacious apartment. Club house available for free all the time. Pet friendly. Smoke free including patio area.\"}";
        return response;
        //Dummy code until real API is available - END
        //Uncomment below lines
        //Response response = request.send();
        //return response.getBody();
    }

    /*public String searchProperties2() {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:1337/searchtest");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }*/

    public static void main(String[] args) {
        WebService ws = new WebService();
        String response = ws.searchProperties("","","","","");

        System.out.println(response);
    }
}

