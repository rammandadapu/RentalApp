package edu.sjsu.cmpe277.rentalapp.rentalapp;

/**
 * Created by divya.chittimalla on 5/4/16.
 */

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;
import edu.sjsu.cmpe277.rentalapp.gcm.RegistrationIntentService;
import edu.sjsu.cmpe277.rentalapp.pojo.Property;



public class WebService {

    private static final String SERVER_URL = "http://52.9.217.97:1337/";
    private static final String LOCAL_URL = "http://127.0.0.1:1337/";
    private static final String MOBILE_URL = "http://10.0.2.2:1337/";
    public static String baseURL;
    private static boolean localMode = false;


    public WebService() {
        if(localMode) {
            baseURL = MOBILE_URL;
        }
        else {
            baseURL = SERVER_URL;
        }
    }



    /**
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
        OAuthRequest request = new OAuthRequest(Verb.GET, baseURL+"searchtest");
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
        } catch (Exception ex) {
            //Failed to connect to server
            Log.e("Server con Failed", ex.getMessage());
            return "connection failed";
        }
    }

    public String postProperty(Context context,Property property){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(null!=property.getImageUrl()) {
            File files = new File(property.getImageUrl());
            try {
                params.put("photos", files);
                params.put("photos", new File(property.getImageUrl()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        params.put("property", prepareJson(property));
        client.post(context, baseURL + "postproperty", params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.print("Failed..");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.print("Success..");
            }
        });

        return "";
    }

    public void updateProperty(Property property, String propertyId,Context context) {
        String requestStr = baseURL+"property/" + propertyId+"/update/";
       /* OAuthRequest request = new OAuthRequest(Verb.PUT, requestStr);
        request.addBodyParameter("post", prepareJson(property));
        request.send();*/
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(null!=property.getImageUrl()) {
            try {
                File files = new File(property.getImageUrl());
                params.put("photos", files);
                params.put("photos", new File(property.getImageUrl()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        params.put("property",prepareJson(property));
        client.put(context, requestStr, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.print("Failed..");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.print("Success..");
            }
        });


        //return response.getBody();
    }

    /***
     * @param property
     * @return true if server response code is 201
     * false if server response code is not equal to 201
     */
   /*public String postProperty(Property property) {
        try {

            OAuthRequest request = new OAuthRequest(Verb.POST, baseURL + "postproperty");
            request.addBodyParameter("post", prepareJson(property));

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
    }*/



    private String prepareJson(Property property) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(property);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    public String getPropertyDetails(String _id) {

        String requestStr = baseURL+"property/" + _id;
        OAuthRequest request = new OAuthRequest(Verb.GET, requestStr);
        //Dummy code until real API is available - START
        //String response = "{\"_id\":\"572835524255d0944af1c63d\",\"address\":{\"line1\":\"1 S Market St Apt 502\",\"city\":\"San Jose\",\"state\":\"CA\",\"zip\":\"95113\"},\"type\":\"house\",\"roomsNo\":3,\"bathNo\":2,\"size\":1440,\"price\":5560,\"phone\":\"(238)-434-676\",\"email\":\"test@mail.com\",\"desc\":\"Large spacious apartment. Club house available for free all the time. Pet friendly. Smoke free including patio area.\"}";
        //return response;
        //Dummy code until real API is available - END
        //Uncomment below lines
        Response response = request.send();
        return response.getBody();
    }

    public String changePropetyStatus(String newStatus,String propertyId) {
        String requestStr = baseURL+"property/" + propertyId+"/status/"+newStatus;
        OAuthRequest request = new OAuthRequest(Verb.POST, requestStr);
        Response response = request.send();
        return response.getBody();
    }



    public String addSavedSearch(String email, String name, String keyword, String location, String priceLow, String priceHigh,
                                 String condo, String apartment, String house, String townhouse, String notify) {
        try{
            OAuthRequest request = new OAuthRequest(Verb.POST, baseURL+"saveSearch");
            request.addQuerystringParameter("email", email);
            request.addQuerystringParameter("name", name);
            request.addQuerystringParameter("keyword", keyword);
            request.addQuerystringParameter("pricelow", priceLow);
            request.addQuerystringParameter("pricehigh", priceHigh);
            request.addQuerystringParameter("location", location);
            request.addQuerystringParameter("condo", condo);
            request.addQuerystringParameter("apartment", apartment);
            request.addQuerystringParameter("house", house);
            request.addQuerystringParameter("townhouse", townhouse);
            request.addQuerystringParameter("regId", RegistrationIntentService.registrationId);
            request.addQuerystringParameter("notify", notify);
            Response response = request.send();
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

    public String getSavedSearches(String email) {
        String requestStr = baseURL+"getSavedSearches/" + email;
        OAuthRequest request = new OAuthRequest(Verb.GET, requestStr);
        Response response = request.send();
        return response.getBody();
    }

    public String getSavedSearchResults(String _id) {
        String requestStr = baseURL+"getSavedSearchResults/" + _id;
        OAuthRequest request = new OAuthRequest(Verb.GET, requestStr);
        Response response = request.send();
        return response.getBody();
    }

    public static void main(String[] args) {
        WebService ws = new WebService();
        //String response = ws.getPropertyDetails("\"572ce6cb6007603237f1b13e\")");
        String response = ws.searchProperties("", "San Jose", "0", "10000", "true", "true", "true", "true", "");

        System.out.println(response);
    }
}

