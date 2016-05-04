package edu.sjsu.cmpe277.rentalapp.rentalapp;

/**
 * Created by divya.chittimalla on 5/4/16.
 */

import android.content.Context;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;


public class WebService {

    OAuthService service;

    public String searchProperties(String keyword, String location, String priceLow, String priceHigh, String type) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://127.0.0.1:1337/searchtest");
        request.addQuerystringParameter("keyword", keyword);
        request.addQuerystringParameter("pricelow", priceLow);
        request.addQuerystringParameter("pricehigh", priceHigh);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("type", type);
        //this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public static void main(String[] args) {
        WebService ws = new WebService();
        String response = ws.searchProperties("club","","","","");

        System.out.println(response);
    }
}

