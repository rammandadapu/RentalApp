package edu.sjsu.cmpe277.rentalapp.rentalapp;

/**
 * Created by divya.chittimalla on 5/4/16.
 */

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.IOException;
import java.net.HttpURLConnection;

import edu.sjsu.cmpe277.rentalapp.pojo.Property;


public class WebService {

    OAuthService service;
    HttpURLConnection urlConnection;

    private static final String SERVER_URL = "http://10.0.2.2:1337/";
    private static final String LOCAL_URL = "http://127.0.0.1:1337/";

    /**
     *
     * @param keyword
     * @param location
     * @param priceLow
     * @param priceHigh
     * @param condo
     * @param apartment
     * @param house
     * @param townhouse
     * @param createdBy
     * @return
     */
    public String searchProperties(String keyword, String location, String priceLow, String priceHigh,
                                   String condo, String apartment, String house, String townhouse,String createdBy) {
        try{
        OAuthRequest request = new OAuthRequest(Verb.GET, SERVER_URL+"searchtest");
        request.addQuerystringParameter("keyword", keyword);
        request.addQuerystringParameter("pricelow", priceLow);
        request.addQuerystringParameter("pricehigh", priceHigh);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("condo", condo);
        request.addQuerystringParameter("apartment", apartment);
        request.addQuerystringParameter("house", house);
        request.addQuerystringParameter("townhouse", townhouse);
        request.addQuerystringParameter("createdBy", createdBy);
        System.out.println("HIIIIIII" + request.toString());
        Response response = request.send();
        System.out.println("HELLOOOOO"+response.getBody());
        return response.getBody();
        }
        catch (RuntimeException runTimeException){
            Log.e("Server con Failed",runTimeException.getMessage());
            return "connection failed";
        }
        catch (Exception ex){
            //Failed to connect to server
            Log.e("Server con Failed",ex.getMessage());
            return "connection failed";
        }
    }


    /***
     * @param property
     * @return true if server response code is 201
     * false if server response code is not equal to 201
     */
    public String postProperty(Property property) {
        try {
            OAuthRequest request = new OAuthRequest(Verb.POST, SERVER_URL + "postproperty");
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = null;
            try {
                requestBody = objectMapper.writeValueAsString(property);
            } catch (IOException e) {
                e.printStackTrace();
            }
            request.addBodyParameter("post", requestBody);
            Response response = request.send();
            System.out.println(response.getBody());
            return response.getBody();
        }
        catch (RuntimeException runTimeException){
            Log.e("Server con Failed",runTimeException.getMessage());
        }
        catch (Exception ex){
            //Failed to connect to server
            Log.e("Server con Fail",ex.getMessage());
        }
        return null;
    }



    public String getPropertyDetails(String _id) {
        String requestStr = "http://10.0.2.2:1337/property/" + _id;
        OAuthRequest request = new OAuthRequest(Verb.GET, requestStr);
        //Dummy code until real API is available - START
        //String response = "{\"_id\":\"572835524255d0944af1c63d\",\"address\":{\"line1\":\"1 S Market St Apt 502\",\"city\":\"San Jose\",\"state\":\"CA\",\"zip\":\"95113\"},\"type\":\"house\",\"roomsNo\":3,\"bathNo\":2,\"size\":1440,\"price\":5560,\"phone\":\"(238)-434-676\",\"email\":\"test@mail.com\",\"desc\":\"Large spacious apartment. Club house available for free all the time. Pet friendly. Smoke free including patio area.\"}";
        //return response;
        //Dummy code until real API is available - END
        //Uncomment below lines
        Response response = request.send();
        return response.getBody();
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
        //String response = ws.getPropertyDetails("\"572ce6cb6007603237f1b13e\")");
        String response = ws.searchProperties("", "San Jose", "0", "10000", "true", "true", "true", "true", "");

        System.out.println(response);
    }
}

